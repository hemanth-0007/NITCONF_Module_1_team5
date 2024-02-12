package com.nitconfbackend.nitconf.types;
import com.nitconfbackend.nitconf.models.Status;

import java.util.List;

public class SessionRequest {
    public String title;
    public String description;
    public Status status;
    public List<String> tags;
}
