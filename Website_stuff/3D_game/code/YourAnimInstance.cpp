// Copyright 1998-2013 Epic Games, Inc. All Rights Reserved.

#include "Project3D.h"
#include "YourAnimInstance.h"


//////////////////////////////////////////////////////////////////////////
// UYourAnimInstance

UYourAnimInstance::UYourAnimInstance(const class FPostConstructInitializeProperties& PCIP)
	: Super(PCIP)
{
	//set any default values for your variables here
	SkelControl_LeftUpperLegPos = FVector(0, 0, 0);
}