package net.sonicrushxii.beyondthehorizon.timehandler;

import net.sonicrushxii.beyondthehorizon.capabilities.all.FormProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public enum TimeAffector
{
    //Add New Time Slowing Abilities in here
    POWER_BOOST("powerBoost",2.5f);

    private final String fieldname;
    public final AtomicBoolean noPlayerPresent;
    public final List<Boolean> dimensionalUsers;
    private final float timeFactor;

    TimeAffector(String fieldname, float timeFactor)
    {
        this.fieldname = fieldname;
        this.noPlayerPresent = new AtomicBoolean(true);
        this.dimensionalUsers = new ArrayList<>();
        this.timeFactor = timeFactor;
    }

    public boolean isContainedBy(FormProperties formProperties) throws NoSuchFieldException, IllegalAccessException
    {
        return formProperties.getClass().getField(this.fieldname).getBoolean(formProperties);
    }

    public float getTimeFactor(){
        return timeFactor;
    }
}
