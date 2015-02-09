// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "GameFramework/Character.h"
#include "Project3DCharacter.h"
#include "Project3DHUD.h"
#include "Perception/PawnSensingComponent.h"
#include "YourAnimInstance.h"
#include "Enemy_Type_One.generated.h"


enum state{ patrol = 1, chase = 2, stop = 3 , fight = 4, death = 5};

UCLASS(config = Game)
class PROJECT3D_API AEnemy_Type_One : public ACharacter
{
	
	GENERATED_UCLASS_BODY()

	UPROPERTY(VisibleAnywhere, BlueprintReadOnly, Category = Awareness)
	TSubobjectPtr<class UPawnSensingComponent> PawnSensor;
	
	
	UPROPERTY(VisibleAnywhere, BlueprintReadOnly, Category = Health)
	TSubclassOf<AHUD> HUDClass;
		//<class Project3DHud> characterHUD;

	bool isFighting();

	UPROPERTY(EditDefaultsOnly, Category = Animation)
	UAnimMontage* AttackAnimationMontage;
	
	UFUNCTION(BlueprintCallable, Category = Path)
		void addTargetPoint(AActor *TargetPoint);

	UFUNCTION(BlueprintCallable, Category = Path)
		void turnOnPatrol();

	UFUNCTION()
	void OnHearNoise(APawn *OtherActor, const FVector &Location, float Volume);
	
	UFUNCTION()
	void OnSeePawn(APawn *OtherPawn);
	
	UFUNCTION(BlueprintCallable, Category = Combat)
		void getHit();

	UPROPERTY(VisibleAnywhere, BlueprintReadWrite, Category = Power)
		float numbOfHits = 5.0f;


	bool wasHit = false;

protected:


	virtual void Tick(float DeltaSeconds) override;

protected:

	void hasBeenHit();

	AProject3DHUD *hudInstance;

	class AProject3DHUD* trackHealth();

	AProject3DCharacter *playerCharacter;

	state currentState;

	void Seek(AActor *player, FString type);

	void PatrolPath();

	int currentTargetPointIndex;

	TArray<AActor*> PathPoints;

	void Patrol();

	int punchTicks = 0;
	
	float health; 

	
	void ThorwPunch(float distance);

	// UPawnSensingComponent Delegates
	void PostInitializeComponents();
	


};
