package lms;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String name;
    private long id;

    public void setName(String name)
    {
        this.name = name;
    }
    public void setId(long id)
    {
        this.id= id;
    }
    public String getName()
    {
        return this.name;
    }
    public long getId()
    {
        return this.id;
    }
}
