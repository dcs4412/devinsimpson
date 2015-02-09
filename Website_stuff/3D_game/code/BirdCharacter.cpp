// Fill out your copyright notice in the Description page of Project Settings.

#include "Project3D.h"
#include "BirdCharacter.h"
#include "Project3DProjectile.h"
#include "Project3DCharacter.h"
#include "Animation/AnimInstance.h"
#include "Engine.h"

ABirdCharacter::ABirdCharacter(const class FPostConstructInitializeProperties& PCIP)
	: Super(PCIP)
{

	// Set size for collision capsule
	CapsuleComponent->InitCapsuleSize(42.f, 96.0f);

	// set our turn rates for input
	BaseTurnRate = 45.f;
	BaseLookUpRate = 45.f;

	// Create a camera boom (pulls in towards the player if there is a collision)
	CameraBoom = PCIP.CreateDefaultSubobject<USpringArmComponent>(this, TEXT("CameraBoom"));
	CameraBoom->AttachTo(RootComponent);
	CameraBoom->TargetArmLength = 300.0f; // The camera follows at this distance behind the character	
	CameraBoom->bUsePawnControlRotation = true; // Rotate the arm based on the controller

	// Create a follow camera
	FollowCamera = PCIP.CreateDefaultSubobject<UCameraComponent>(this, TEXT("FollowCamera"));
	FollowCamera->AttachTo(CameraBoom, USpringArmComponent::SocketName); // Attach the camera to the end of the boom and let the boom adjust to match the controller orientation
	FollowCamera->bUsePawnControlRotation = false; // Camera does not rotate relative to arm
	
	Mesh_Bird = PCIP.CreateDefaultSubobject<USkeletalMeshComponent>(this, TEXT("CharacterMesh_Bird"));
	Mesh_Bird->SetOnlyOwnerSee(true);			// only the owning player will see this mesh
	Mesh_Bird->AttachParent = FollowCamera;
	Mesh_Bird->RelativeLocation = FVector(0.f, 0.f, -150.f);
	Mesh_Bird->bCastDynamicShadow = false;
	Mesh_Bird->CastShadow = false;

	CharacterMovement->SetMovementMode(MOVE_Flying);

	currentState = follow;

	// Note: The ProjectileClass and the skeletal mesh/anim blueprints for Mesh_Bird are set in the
	// derived blueprint asset named MyCharacter (to avoid direct content references in C++)
}

//////////////////////////////////////////////////////////////////////////
// Input

void ABirdCharacter::SetupPlayerInputComponent(class UInputComponent* InputComponent)
{
	// set up gameplay key bindings
	check(InputComponent);

	InputComponent->BindAction("Jump", IE_Pressed, this, &ACharacter::Jump);
	InputComponent->BindAction("Jump", IE_Released, this, &ACharacter::StopJumping);

	InputComponent->BindAxis("MoveForward", this, &ABirdCharacter::MoveForward);
	InputComponent->BindAxis("MoveRight", this, &ABirdCharacter::MoveRight);

	// We have 2 versions of the rotation bindings to handle different kinds of devices differently
	// "turn" handles devices that provide an absolute delta, such as a mouse.
	// "turnrate" is for devices that we choose to treat as a rate of change, such as an analog joystick
	InputComponent->BindAxis("Turn", this, &APawn::AddControllerYawInput);
	InputComponent->BindAxis("TurnRate", this, &ABirdCharacter::TurnAtRate);
	InputComponent->BindAxis("LookUp", this, &APawn::AddControllerPitchInput);
	InputComponent->BindAxis("LookUpRate", this, &ABirdCharacter::LookUpAtRate);
}


void ABirdCharacter::MoveForward(float Value)
{
	if (Value != 0.0f)
	{
		// add movement in that direction
		AddMovementInput(GetActorForwardVector(), Value);
	}
}

void ABirdCharacter::MoveRight(float Value)
{
	if (Value != 0.0f)
	{
		// add movement in that direction
		AddMovementInput(GetActorRightVector(), Value);
	}
}

void ABirdCharacter::TurnAtRate(float Rate)
{
	// calculate delta for this frame from the rate information
	AddControllerYawInput(Rate * BaseTurnRate * GetWorld()->GetDeltaSeconds());
}

void ABirdCharacter::LookUpAtRate(float Rate)
{
	// calculate delta for this frame from the rate information
	AddControllerPitchInput(Rate * BaseLookUpRate * GetWorld()->GetDeltaSeconds());
}

void ABirdCharacter::Tick(float DeltaSeconds)
{
		Super::Tick(DeltaSeconds);

		for (FConstPawnIterator It = GetWorld()->GetPawnIterator(); It; ++It)
		{
			AProject3DCharacter *TestPawn = Cast<AProject3DCharacter>(*It);
			if (TestPawn)
			{
				const float DistSq = FVector::Dist(TestPawn->GetActorLocation(), CharacterMovement->GetActorLocation());
				if (TestPawn)
				{

					if (currentState == follow){
						Seek(TestPawn, FString::FString("player"));
					}
					
				}
			}
		}
		
		

}

void ABirdCharacter::Seek(AActor *player, FString type){

	FVector temp = player->GetActorLocation() - CharacterMovement->GetActorLocation();
	FVector DesiredVelocity = temp.UnsafeNormal() * CharacterMovement->GetMaxSpeed();

	FVector move = DesiredVelocity - GetVelocity();
	FVector playerLoc = player->GetActorLocation();
	FRotator rotate = (playerLoc - CharacterMovement->GetActorLocation()).Rotation();
	SetActorRotation(rotate);

	
	const float DistSq = FVector::Dist(player->GetActorLocation(), CharacterMovement->GetActorLocation());
	if (DistSq > 150.f){
		AddMovementInput(move, 0.4);
		//GEngine->AddOnScreenDebugMessage(-1, 5.f, FColor::Green, FString::FString("Stupid godamn Bird! ") + FString::SanitizeFloat(move[0]) + FString::FString(" ")+FString::SanitizeFloat(move[1]) + FString::FString(" ")+FString::SanitizeFloat(move[2]));

	}

}

void ABirdCharacter::bePossessed()
{
	currentState = control;

	
}



void ABirdCharacter::unPossess()
{
	currentState = follow;


}