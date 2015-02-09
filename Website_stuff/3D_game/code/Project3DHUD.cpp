// Copyright 1998-2014 Epic Games, Inc. All Rights Reserved.

#include "Project3D.h"
#include "Project3DHUD.h"
#include "Enemy_Type_One.h"
#include "Engine/Canvas.h"
#include "TextureResource.h"
#include "CanvasItem.h"
#include "Engine.h"

AProject3DHUD::AProject3DHUD(const class FPostConstructInitializeProperties& PCIP) : Super(PCIP)
{
	// Set the crosshair texture
//	static ConstructorHelpers::FObjectFinder<UTexture2D> CrosshiarTexObj(TEXT("/Game/Textures/Crosshair"));
//	CrosshairTex = CrosshiarTexObj.Object;
	static ConstructorHelpers::FObjectFinder<UTexture2D> HealthTextObj(TEXT("/Game/Textures/health_texture"));
	HealthTex = HealthTextObj.Object; 
	
	

}


void AProject3DHUD::DrawHUD()
{
	Super::DrawHUD();
	DrawHealth(healthCounter);
	FVector2D ScreenDimensions = FVector2D(Canvas->SizeX, Canvas->SizeY);

	if (healthCounter < 1)
	{
		DrawText(TEXT("GAME OVER"), FColor::White, (ScreenDimensions.X - ScreenDimensions.X / 10) / 2.f, (ScreenDimensions.Y - ScreenDimensions.Y / 10) / 2.f);
		for (FConstPawnIterator It = GetWorld()->GetPawnIterator(); It; ++It)
		{

			AProject3DCharacter *TestPawn = Cast<AProject3DCharacter>(*It);

			if (TestPawn){

				TestPawn->death();

			}

		}
	}
	else{

		float counter = 0;

		for (FConstPawnIterator It = GetWorld()->GetPawnIterator(); It; ++It)
		{
			AEnemy_Type_One *MyGuard = Cast<AEnemy_Type_One>(*It);
			if (MyGuard) {

				if (MyGuard->isFighting() == true){

					if (MyGuard->wasHit)
					{

						float hits = abs(MyGuard->numbOfHits);

						if (previousHits != hits)
						{
							hits = 1;
							healthCounter = healthCounter - hits;
							DrawHealth(healthCounter);


							previousHits = hits;
						}
					}
				}
			}
		}
	}
}


void AProject3DHUD::TrackHealth()
{
	healthCounter--;
	DrawHealth(healthCounter);

}

void AProject3DHUD::DrawHealth(float Health)
{
	Super::DrawHUD();



	const FVector2D Center(Canvas->ClipX * 0.1f, Canvas->ClipY * 0.1f);

	if (Health > 0)
	{
		const FVector2D HealthDrawPosition((Center.X - (HealthTex->GetSurfaceWidth() * 0.5f)),
			(Center.Y - (HealthTex->GetSurfaceHeight() * 0.5f)));

		FCanvasTileItem HealthItem(HealthDrawPosition, HealthTex->Resource, FLinearColor::White);
		HealthItem.BlendMode = SE_BLEND_Translucent;
		Canvas->DrawItem(HealthItem);
	}
	if (Health > 1)
	{
		const FVector2D HealthDrawPosition1((Center.X - (HealthTex->GetSurfaceWidth() * 0.5f) + 16),
			(Center.Y - (HealthTex->GetSurfaceHeight() * 0.5f)));
		FCanvasTileItem HealthItem1(HealthDrawPosition1, HealthTex->Resource, FLinearColor::White);
		HealthItem1.BlendMode = SE_BLEND_Translucent;
		Canvas->DrawItem(HealthItem1);
	}
	if (Health > 2)
	{
		const FVector2D HealthDrawPosition2((Center.X - (HealthTex->GetSurfaceWidth() * 0.5f) + 32),
			(Center.Y - (HealthTex->GetSurfaceHeight() * 0.5f)));
		FCanvasTileItem HealthItem2(HealthDrawPosition2, HealthTex->Resource, FLinearColor::White);
		HealthItem2.BlendMode = SE_BLEND_Translucent;
		Canvas->DrawItem(HealthItem2);
	}
	if (Health > 3)
	{
		const FVector2D HealthDrawPosition3((Center.X - (HealthTex->GetSurfaceWidth() * 0.5f) + 48),
			(Center.Y - (HealthTex->GetSurfaceHeight() * 0.5f)));
		FCanvasTileItem HealthItem3(HealthDrawPosition3, HealthTex->Resource, FLinearColor::White);
		HealthItem3.BlendMode = SE_BLEND_Translucent;
		Canvas->DrawItem(HealthItem3);
	}
	if (Health > 4)
	{
	const FVector2D HealthDrawPosition4((Center.X - (HealthTex->GetSurfaceWidth() * 0.5f) + 60),
		(Center.Y - (HealthTex->GetSurfaceHeight() * 0.5f)));
	FCanvasTileItem HealthItem4(HealthDrawPosition4, HealthTex->Resource, FLinearColor::White);
	HealthItem4.BlendMode = SE_BLEND_Translucent;
	Canvas->DrawItem(HealthItem4);
	}
}

