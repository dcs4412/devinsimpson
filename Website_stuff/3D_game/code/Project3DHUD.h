// Copyright 1998-2014 Epic Games, Inc. All Rights Reserved.
#pragma once 
#include "GameFramework/HUD.h"
#include "Project3DHUD.generated.h"

UCLASS(config = Game)
class AProject3DHUD : public AHUD
{
	GENERATED_UCLASS_BODY()

public:
	/** Variable for storing the font. */
	UPROPERTY()
		UFont* HUDFont;

	/** Primary draw call for the HUD */
	virtual void DrawHUD() override;

	UFUNCTION(BlueprintCallable, Category = HUD)
	void  TrackHealth();
	void  DrawHealth(float Health);
	float healthCounter = 5.0f;
	float previousHits = 0;

private:
	/** Crosshair asset pointer */
//	class UTexture2D* CrosshairTex;
	class UTexture2D* HealthTex;
};

