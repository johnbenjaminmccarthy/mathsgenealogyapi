package com.mathsgenealogyapi.scraper;


import java.util.List;
import java.util.Objects;
import java.util.Optional;


public record ScrapedDissertationData (
    Integer studentId,
    String studentName,
    String phdprefix,
    String university,
    String yearofcompletion,
    String dissertationtitle,
    String mscnumber,
    List<ScrapedAdvisorData> advisors
) {

    Optional<ScrapedAdvisorData> getAdvisorByNumber(Integer number) {
        for (ScrapedAdvisorData advisor : advisors) {
            if (Objects.equals(advisor.advisorNumber(), number)) {
                return Optional.of(advisor);
            }
        }
        return Optional.empty();
    }
}
