package com.bumppo109.firma_compat.blockentity;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.blockentity.FluidBrewingStandMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class FluidBrewingStandScreen
        extends AbstractContainerScreen<FluidBrewingStandMenu> {

    /*
     * Vanilla brewing stand GUI texture.
     *
     * If you want your own artwork later, replace this with:
     *
     * ResourceLocation.fromNamespaceAndPath(
     *     FirmaCompat.MODID,
     *     "textures/gui/fluid_brewing_stand.png"
     * )
     */
    private static final ResourceLocation BREWING_STAND_LOCATION =
            ResourceLocation.withDefaultNamespace(
                    "textures/gui/container/brewing_stand.png"
            );

    /*
     * Vanilla brewing stand dimensions.
     */
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 166;

    /*
     * Vanilla fuel sprite.
     */
    private static final ResourceLocation FUEL_LENGTH_SPRITE =
            ResourceLocation.withDefaultNamespace(
                    "container/brewing_stand/fuel_length"
            );

    /*
     * Vanilla brewing progress sprite.
     */
    private static final ResourceLocation BREW_PROGRESS_SPRITE =
            ResourceLocation.withDefaultNamespace(
                    "container/brewing_stand/brew_progress"
            );

    /*
     * Vanilla bubble animation.
     */
    private static final ResourceLocation BUBBLES_SPRITE =
            ResourceLocation.withDefaultNamespace(
                    "container/brewing_stand/bubbles"
            );

    /*
     * Exactly the same bubble lengths used by vanilla.
     */
    private static final int[] BUBBLE_LENGTHS =
            new int[]{
                    29,
                    24,
                    20,
                    16,
                    11,
                    6,
                    0
            };

    public FluidBrewingStandScreen(
            FluidBrewingStandMenu menu,
            Inventory playerInventory,
            Component title
    ) {
        super(
                menu,
                playerInventory,
                title
        );

        imageWidth = GUI_WIDTH;
        imageHeight = GUI_HEIGHT;
    }

    /*
     * ------------------------------------------------------------
     * INIT
     * ------------------------------------------------------------
     */

    @Override
    protected void init() {
        super.init();

        /*
         * Center the title exactly like vanilla BrewingStandScreen.
         */
        titleLabelX =
                (imageWidth - font.width(title)) / 2;
    }

    /*
     * ------------------------------------------------------------
     * BACKGROUND
     * ------------------------------------------------------------
     */

    @Override
    protected void renderBg(
            GuiGraphics guiGraphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {
        /*
         * Match vanilla.
         */
        int left =
                (width - imageWidth) / 2;

        int top =
                (height - imageHeight) / 2;

        /*
         * Base GUI.
         */
        guiGraphics.blit(
                BREWING_STAND_LOCATION,
                left,
                top,
                0,
                0,
                imageWidth,
                imageHeight
        );

        /*
         * --------------------------------------------------------
         * FUEL
         * --------------------------------------------------------
         *
         * Vanilla:
         *
         *     int k = menu.getFuel();
         *     int l = Mth.clamp((18 * k + 20 - 1) / 20, 0, 18);
         *
         * This means 20 fuel units produces a full 18-pixel bar.
         */
        int fuel =
                menu.getFuel();

        int fuelWidth =
                Mth.clamp(
                        (18 * fuel + 20 - 1) / 20,
                        0,
                        18
                );

        if (fuelWidth > 0) {
            guiGraphics.blitSprite(
                    FUEL_LENGTH_SPRITE,
                    18,
                    4,
                    0,
                    0,
                    left + 60,
                    top + 44,
                    fuelWidth,
                    4
            );
        }

        /*
         * --------------------------------------------------------
         * BREWING PROGRESS
         * --------------------------------------------------------
         *
         * Vanilla brewing takes 400 ticks.
         */
        int brewingTicks =
                menu.getBrewingTicks();

        if (brewingTicks > 0) {

            /*
             * Progress arrow.
             */
            int progressHeight =
                    (int) (
                            28.0F
                                    * (
                                    1.0F
                                            - (float) brewingTicks
                                            / 400.0F
                            )
                    );

            if (progressHeight > 0) {
                guiGraphics.blitSprite(
                        BREW_PROGRESS_SPRITE,
                        9,
                        28,
                        0,
                        0,
                        left + 97,
                        top + 16,
                        9,
                        progressHeight
                );
            }

            /*
             * Bubble animation.
             */
            int bubbleLength =
                    BUBBLE_LENGTHS[
                            brewingTicks / 2 % 7
                            ];

            if (bubbleLength > 0) {
                guiGraphics.blitSprite(
                        BUBBLES_SPRITE,
                        12,
                        29,
                        0,
                        29 - bubbleLength,
                        left + 63,
                        top + 14 + 29 - bubbleLength,
                        12,
                        bubbleLength
                );
            }
        }
    }

    /*
     * ------------------------------------------------------------
     * RENDER
     * ------------------------------------------------------------
     */

    @Override
    public void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        super.render(
                guiGraphics,
                mouseX,
                mouseY,
                partialTick
        );

        renderTooltip(
                guiGraphics,
                mouseX,
                mouseY
        );
    }
}
