package org.arabius.platform.util;

import java.util.ArrayList;

import org.arabius.platform.domain.GuideRequirment;

public class FakerGuideRequirments {
    private static ArrayList<GuideRequirment> guideRequirments;

    public FakerGuideRequirments() {
        guideRequirments = new ArrayList<>();
        guideRequirments.add(new GuideRequirment("no_guide", false, false, false));
        guideRequirments.add(new GuideRequirment("guide_optional", true, false, true));
        guideRequirments.add(new GuideRequirment("guide_required", true, true, false));
    }

    public static ArrayList<GuideRequirment> getGuideRequirments() {
        return guideRequirments;
    }
}
