// Copyright 1998-2014 Epic Games, Inc. All Rights Reserved.

#include "Project3D.h"
#include "Project3DGameMode.h"
#include "Project3DHUD.h"
#include "Project3DCharacter.h"


AProject3DGameMode::AProject3DGameMode(const class FPostConstructInitializeProperties& PCIP)
	: Super(PCIP)
{
	// set default pawn class to our Blueprinted character
	static ConstructorHelpers::FClassFinder<APawn> PlayerPawnClassFinder(TEXT("/Game/Blueprints/MyCharacter"));
	DefaultPawnClass = PlayerPawnClassFinder.Class;

	// use our custom HUD class
	HUDClass = AProject3DHUD::StaticClass();
}
