package com.github.lunchvotingsystem.to;

import com.github.lunchvotingsystem.util.validation.NoHtml;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DishTo {
    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    private String name;
    @Range(min = 1)
    @NotNull
    private long cost;
}
