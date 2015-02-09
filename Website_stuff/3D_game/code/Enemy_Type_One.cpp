// Fill out your copyright notice in the Description page of Project Settings.

#include "Project3D.h"
#include "Enemy_Type_One.h"
#include "Project3DHUD.h"
#include "Engine.h"

AEnemy_Type_One::AEnemy_Type_One(const class FPostConstructInitializeProperties& PCIP)
	: Super(PCIP)
{

	health = 2.f;

	PawnSensor = PCIP.CreateDefaultSubobject<UPawnSensingComponent>(this, TEXT("Pawn Sensor"));
	PawnSensor->SensingInterval = 0.25f; //sensor check 4 times per second
	PawnSensor->bOnlySensePlayers = true; //for now, only sense player character
	PawnSensor->SetPeripheralVisionAngle(45.f); //angle of vision
	
	//AProject3DHUD* HudInstance = trackHealth();
	//set up attack montage
	ConstructorHelpers::FObjectFinder < UAnimMontage >  AttackAnimationAsset(TEXT("/Game/Animations/Gaurd_Attack_Montage.Gaurd_Attack_Montage"));
	
	AttackAnimationMontage = AttackAnimationAsset.Object;

	DECLARE_DYNAMIC_MULTICAST_DELEGATE_OneParam(FSeePawnDelegate, APawn*, Pawn);
	DECLARE_DYNAMIC_MULTICAST_DELEGATE_ThreeParams(FHearNoiseDelegate, APawn*, Instigator, const FVector&, Location, float, Volume);
	currentState = stop;
	//TSubclassOf<AHUD> HUDClass;
	HUDClass = AProject3DHUD::StaticClass();
	
}


//////////////////////////////////////////////////////////////////////////
// Input

void AEnemy_Type_One::addTargetPoint(AActor *TargetPoint){

	PathPoints.Add(TargetPoint);
}

void AEnemy_Type_One::turnOnPatrol()
{
	currentState = patrol;
	currentTargetPointIndex = 0;
}

bool AEnemy_Type_One::isFighting(){
	
	if (currentState == fight || currentState == chase){
		return true;
	}
	else{
		return false;
	}

}

void AEnemy_Type_One::hasBeenHit(){

	for (FConstPawnIterator It = GetWorld()->GetPawnIterator(); It; ++It)
	{
		AProject3DCharacter *TestPawn = Cast<AProject3DCharacter>(*It);
		if (TestPawn)
		{
			if (TestPawn->hasAttacked() == true){

				float distance = FVector::Dist(TestPawn->GetActorLocation(), CharacterMovement->GetActorLocation());

				if (distance <= 300.0f){
				
						if (currentState == fight){
							health = health - 1;
							
						}
						else{

							health = 0;

						}

						if (health == 0){
							currentState = death;
							Mesh->SetSimulatePhysics(true);
						}
				}
				else{

				}

				TestPawn->endAttack();
				
			}

		}
	}
}





////////////////////////////////////////////////////////////////////////
//enemy animations
void AEnemy_Type_One::ThorwPunch(float distance){
	wasHit = false;
	if (punchTicks == 90){
		punchTicks = 0;
	}

	if (punchTicks == 0){

		UAnimInstance * AnimInstance = Mesh->GetAnimInstance();
		if (AnimInstance != NULL){
			Mesh->AnimScriptInstance->Montage_Play(AttackAnimationMontage, 0.6f);
		}
	}

	if (punchTicks == 60){

		for (FConstPawnIterator It = GetWorld()->GetPawnIterator(); It; ++It)
		{
			AProject3DCharacter *TestPawn = Cast<AProject3DCharacter>(*It);
			if (TestPawn)
			{

				if (distance <= 160.0f){

					
					numbOfHits--;
					getHit();
					}
				else{

					Seek(TestPawn, FString::FString("player"));

					}	

				}
			}
		}

	punchTicks++;

}

////////////////////////////////////////////////////////////////////////
//update enemy movement
void AEnemy_Type_One::Tick(float DeltaSeconds)
{

	if (health == 0.f){

		currentState = death;
	}else{
		Super::Tick(DeltaSeconds);
		if (currentState == patrol){
			PatrolPath();
		}
		else if (currentState == chase){


			for (FConstPawnIterator It = GetWorld()->GetPawnIterator(); It; ++It)
			{
				AProject3DCharacter *TestPawn = Cast<AProject3DCharacter>(*It);
				if (TestPawn)
				{
					const float DistSq = FVector::Dist(TestPawn->GetActorLocation(), CharacterMovement->GetActorLocation());
					if (TestPawn)
					{
						if (currentState == chase){
							
							Seek(TestPawn, FString::FString("player"));
							
						}
					}
				}
			}
		}
		else if (currentState == fight){


			for (FConstPawnIterator It = GetWorld()->GetPawnIterator(); It; ++It)
			{
				AProject3DCharacter *TestPawn = Cast<AProject3DCharacter>(*It);
				if (TestPawn)
				{
					const float DistSq = FVector::Dist(TestPawn->GetActorLocation(), CharacterMovement->GetActorLocation());
					if (DistSq > 170.0f){
						punchTicks = 0;
						currentState = chase;

					}
					else{
						//GEngine->AddOnScreenDebugMessage(-1, 5.f, FColor::Red, FString::FString("Distance is ") + FString::SanitizeFloat(DistSq));
						ThorwPunch(DistSq);

					}
				}
			}
		}
		hasBeenHit();
	}

}

////////////////////////////////////////////////////////////////////
//enemey movement
void AEnemy_Type_One::PatrolPath(){

	AActor *targetPoint = PathPoints[currentTargetPointIndex];
	Seek(targetPoint, FString::FString("target"));
}

void AEnemy_Type_One::Seek(AActor *player, FString type){

	FVector temp = player->GetActorLocation() - CharacterMovement->GetActorLocation();
	FVector DesiredVelocity = temp.UnsafeNormal() * CharacterMovement->GetMaxSpeed();



	FVector move = DesiredVelocity - GetVelocity();
	FVector playerLoc = player->GetActorLocation();
	FRotator rotate = (playerLoc - CharacterMovement->GetActorLocation()).Rotation();
	SetActorRotation(rotate);
	
	AddMovementInput(move, 0.4);

	const float DistSq = FVector::Dist(player->GetActorLocation(), CharacterMovement->GetActorLocation());
	if (type == FString::FString("target")){
		if (DistSq < 90.0f){
			currentTargetPointIndex++;
			if (currentTargetPointIndex == PathPoints.Num()){
				currentTargetPointIndex = 0;
			}
		}
	}else if (type == FString::FString("player")){

		if (DistSq < 180.0f){

			currentState = fight;
		}
	}
}

////////////////////////////////////////////////////////////////////
//enemy awareness
void AEnemy_Type_One::PostInitializeComponents()
{
	Super::PostInitializeComponents();
	PawnSensor->OnSeePawn.AddDynamic(this, &AEnemy_Type_One::OnSeePawn);
	PawnSensor->OnHearNoise.AddDynamic(this, &AEnemy_Type_One::OnHearNoise);
}

void AEnemy_Type_One::OnHearNoise(APawn *OtherActor, const FVector &Location, float Volume)
{

	const FString VolumeDesc = FString::Printf(TEXT(" at volume %f"), Volume);
	FString message = TEXT("Heard Actor ") + OtherActor->GetName() + VolumeDesc;
	GEngine->AddOnScreenDebugMessage(-1, 5.f, FColor::Red, message);

	// TODO: game-specific logic    
}

void AEnemy_Type_One::OnSeePawn(APawn *OtherPawn)
{
	AProject3DCharacter *TestPawn = Cast<AProject3DCharacter>(OtherPawn);

	if (TestPawn){

		if (currentState != fight){
			currentState = chase;
		}
		
	}
	
}

void AEnemy_Type_One::getHit()
{
	wasHit = true;
}