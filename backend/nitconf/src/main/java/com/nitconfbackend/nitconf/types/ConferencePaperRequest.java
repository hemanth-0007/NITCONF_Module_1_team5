package com.nitconfbackend.nitconf.types;
import com.nitconfbackend.nitconf.models.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ConferencePaperRequest {
    public String title;
    public String description;
    public Status status;
    public List<String> tags;
}
