package com.example.irene.khramovahomework8

class Color {
    companion object {
        fun getColor(itemId: Int): Int {
            return when (itemId) {
                R.id.radioButtonAzure -> R.color.azure
                R.id.radioButtonBarney -> R.color.barney
                R.id.radioButtonBlueGrey -> R.color.blue_grey
                R.id.radioButtonBlueyPurple -> R.color.bluey_purple
                R.id.radioButtonBooger -> R.color.booger
                R.id.radioButtonLightishBlue -> R.color.lightish_blue
                R.id.radioButtonLipstic -> R.color.lipstick
                R.id.radioButtonLipsticTwo -> R.color.lipstick_two
                R.id.radioButtonMarigold -> R.color.marigold
                R.id.radioButtonOrangeRed -> R.color.orange_red
                R.id.radioButtonSicklyYellow -> R.color.sickly_yellow
                R.id.radioButtonSunshineYellow -> R.color.sunshine_yellow
                R.id.radioButtonTeal -> R.color.teal
                R.id.radioButtonTurquoiseBlue -> R.color.turquoise_blue
                R.id.radioButtonWarmGreyFive -> R.color.warm_grey_five
                else -> throw IllegalArgumentException("Invalid color param value")
            }
        }
    }
}