// Copyright 1998-2014 Epic Games, Inc. All Rights Reserved.

#include "Project3D.h"
#include "Project3DCharacter.h"
#include "Project3DProjectile.h"
#include "Animation/AnimInstance.h"
#include "Engine.h"

//////////////////////////////////////////////////////////////////////////
// AProject3DCharacter

AProject3DCharacter::AProject3DCharacter(const class FPostConstructInitializeProperties& PCIP)
	: Super(PCIP)
{
	// Set size for collision capsule
	CapsuleComponent->InitCapsuleSize(42.f, 96.0f);

	// set our turn rates for input
	BaseTurnRate = 45.f;
	BaseLookUpRate = 45.f;

	//set up attack montage
	ConstructorHelpers::FObjectFinder < UAnimMontage > AttackAnimationAsset(TEXT("/Game/Animations/player_attack.player_attack"));
	AttackAnimationMontage = AttackAnimationAsset.Object;
	
	// Create a CameraComponent	
	FirstPersonCameraComponent = PCIP.CreateDefaultSubobject<UCameraComponent>(this, TEXT("FirstPersonCamera"));
	FirstPersonCameraComponent->AttachParent = CapsuleComponent;
	FirstPersonCameraComponent->RelativeLocation = FVector(0, 0, 64.f); // Position the camera
	FirstPersonCameraComponent->bUsePawnControlRotation = true;

	// Default offset from the character location for projectiles to spawn
	GunOffset = FVector(100.0f, 30.0f, 10.0f);

	//Create a mesh component that will be used when being viewed from a '1st person' view (when controlling this pawn)
	Mesh1P = PCIP.CreateDefaultSubobject<USkeletalMeshComponent>(this, TEXT("CharacterMesh1P"));
	Mesh1P->SetOnlyOwnerSee(true);			// only the owning player will see this mesh
    Mesh1P->AttachParent = FirstPersonCameraComponent;
	Mesh1P->RelativeLocation = FVector(0.f, 0.f, -150.f);
    Mesh1P->bCastDynamicShadow = false;
    Mesh1P->CastShadow = false;
	
	attacking = false;

	playState = preStart;
}

//////////////////////////////////////////////////////////////////////////
// Input

void AProject3DCharacter::SetupPlayerInputComponent(class UInputComponent* InputComponent)
{
	// set up gameplay key bindings
	check(InputComponent);

	InputComponent->BindAction("Jump", IE_Pressed, this, &ACharacter::Jump);
	InputComponent->BindAction("Jump", IE_Released, this, &ACharacter::StopJumping);
	
	InputComponent->BindAction("Fire", IE_Pressed, this, &AProject3DCharacter::OnFire);
	InputComponent->BindTouch(EInputEvent::IE_Pressed, this, &AProject3DCharacter::TouchStarted);

	InputComponent->BindAxis("MoveForward", this, &AProject3DCharacter::MoveForward);
	InputComponent->BindAxis("MoveRight", this, &AProject3DCharacter::MoveRight);
	
	// We have 2 versions of the rotation bindings to handle different kinds of devices differently
	// "turn" handles devices that provide an absolute delta, such as a mouse.
	// "turnrate" is for devices that we choose to treat as a rate of change, such as an analog joystick
	InputComponent->BindAxis("Turn", this, &APawn::AddControllerYawInput);
	InputComponent->BindAxis("TurnRate", this, &AProject3DCharacter::TurnAtRate);
	InputComponent->BindAxis("LookUp", this, &APawn::AddControllerPitchInput);
	InputComponent->BindAxis("LookUpRate", this, &AProject3DCharacter::LookUpAtRate);
}

void AProject3DCharacter::death(){

	for (FConstPlayerControllerIterator Iterator = GetWorld()->GetPlayerControllerIterator(); Iterator; ++Iterator)
	{
		APlayerController *PC = Cast<APlayerController>(*Iterator);
		if (PC)
		{
			PC->ConsoleCommand("RestartLevel", false);
		}
	}

}

void AProject3DCharacter::OnFire()
{
	// try and play the sound if specified
	if (FireSound != NULL)
	{
		//UGameplayStatics::PlaySoundAtLocation(this, FireSound, GetActorLocation());
	}

	// try and play a firing animation if specified
	if (AttackAnimationMontage != NULL)
	{
		
		// Get the animation object for the arms mesh
		UAnimInstance* AnimInstance = Mesh->GetAnimInstance();
		if(AnimInstance != NULL)
		{

			

			if (characterTicks < 10000)
			{
				//GEngine->AddOnScreenDebugMessage(1, 2.0, FColor::Red, FString::FString("characterTicks ") + FString::SanitizeFloat(characterTicks));
				AnimInstance->Montage_Play(AttackAnimationMontage, 1.5f);
				attacking = true;
				characterTicks = 10000;
			}
		}
		//characterTicks++;
		if (characterTicks > 150)
		{
			characterTicks = 0;
		}
	}

}
void AProject3DCharacter::Tick(float DeltaSeconds)
{
	
	Super::Tick(DeltaSeconds);
	characterTicks++;
	if (playState == play)
	{
		FVector endLoc = endPoint->GetActorLocation();
		FVector playerLoc = CharacterMovement->GetActorLocation();
		const float DistSq = FVector::Dist(playerLoc, endLoc);
		if (DistSq < 100.0){
			GEngine->AddOnScreenDebugMessage(1, 5.0, FColor::Green, FString::FString("END STATE"));
			Destroy();
		}
	}
	
}

bool AProject3DCharacter::hasAttacked(){

	return attacking;
	
}

void AProject3DCharacter::setEndPoint(AActor *endTargetPoint)
{

	endPoint = endTargetPoint;
	playState = play;

}

void AProject3DCharacter::endAttack(){

	gaurdCount = gaurdCount + 1;

	if (gaurdCount == 3){

		gaurdCount = 0;

		attacking = false;

	}

}

void AProject3DCharacter::TouchStarted(const ETouchIndex::Type FingerIndex, const FVector Location)
{
	// only fire for first finger down
	if (FingerIndex == 0)
	{
		OnFire();
	}
}

void AProject3DCharacter::MoveForward(float Value)
{
	if (Value != 0.0f)
	{
		// add movement in that direction
		AddMovementInput(GetActorForwardVector(), Value);
	}
}

void AProject3DCharacter::MoveRight(float Value)
{
	if (Value != 0.0f)
	{
		// add movement in that direction
		AddMovementInput(GetActorRightVector(), Value);
	}
}

void AProject3DCharacter::TurnAtRate(float Rate)
{
	// calculate delta for this frame from the rate information
	AddControllerYawInput(Rate * BaseTurnRate * GetWorld()->GetDeltaSeconds());
}

void AProject3DCharacter::LookUpAtRate(float Rate)
{
	// calculate delta for this frame from the rate information
	AddControllerPitchInput(Rate * BaseLookUpRate * GetWorld()->GetDeltaSeconds());
}
