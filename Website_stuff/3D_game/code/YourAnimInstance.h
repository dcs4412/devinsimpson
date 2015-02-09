// Fill out your copyright notice in the Description page of Project Settings.

#pragma once

#include "Animation/AnimInstance.h"
#include "YourAnimInstance.generated.h"

UCLASS(transient, Blueprintable, hideCategories = AnimInstance, BlueprintType)
class UYourAnimInstance : public UAnimInstance
{
	GENERATED_UCLASS_BODY()

		/** Left Lower Leg Offset From Ground, Set in Character.cpp Tick */
		UPROPERTY(EditAnywhere, BlueprintReadWrite, Category = FootPlacement)
		FVector SkelControl_LeftLowerLegPos;

	/** Left Foot Rotation, Set in Character.cpp Tick */
	UPROPERTY(EditAnywhere, BlueprintReadWrite, Category = FootPlacement)
		FRotator SkelControl_LeftFootRotation;

	/** Left Upper Leg Offset, Set in Character.cpp Tick */
	UPROPERTY(EditAnywhere, BlueprintReadWrite, Category = FootPlacement)
		FVector SkelControl_LeftUpperLegPos;
};