package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditsDTO {
    private List<CastDTO> cast;
    private List<CrewDTO> crew;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CastDTO {
        private Long id;
        private String name;
        private String character;
        private Integer gender;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CrewDTO {
        private Long id;
        private String name;
        private String job;
        private String department;
    }
}