package com.example.android.moneyanalytics.chart;

import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;

/**
 * Class used to initialize the default config params ot the Pie Chart.
 */
public class DefaultPieConfig {

    public DefaultPieConfig(){}

    public AnimatedPieViewConfig getDefaultPieConfig(boolean canTouch) {
        return new AnimatedPieViewConfig()
                .startAngle(-90)
                .strokeWidth(200)
                .canTouch(canTouch)
                .drawText(true)
                .textSize(80)
                .textMargin(8)
                .guidePointRadius(8)
                .guideLineWidth(6)
                .textGravity(AnimatedPieViewConfig.ECTOPIC)
                .duration(700);
    }
}
