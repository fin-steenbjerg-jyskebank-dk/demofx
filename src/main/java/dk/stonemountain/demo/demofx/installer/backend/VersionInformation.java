package dk.stonemountain.demo.demofx.installer.backend;

import java.time.ZonedDateTime;

import javax.json.bind.annotation.JsonbProperty;

public class VersionInformation {
    @JsonbProperty("current-version-must-be-updated")
    public Boolean mustBeUpdated;
    @JsonbProperty("current-version-is-working")
    public Boolean currentIsWorking;
    @JsonbProperty("recommended-version")
    public String recommendedVersion;
    @JsonbProperty("recommended-sha")
    public String recommendedSha;
    @JsonbProperty("recommended-release-note")
    public String recommendedReleaseNote;
    @JsonbProperty("recommended-release-time")
    public ZonedDateTime recommendedReleaseTime;

    @Override
    public String toString() {
        return "VersionInformation [currentIsWorking=" + currentIsWorking + ", mustBeUpdated=" + mustBeUpdated
                + ", recommendedReleaseNote=" + recommendedReleaseNote + ", recommendedReleaseTime="
                + recommendedReleaseTime + ", recommendedSha=" + recommendedSha + ", recommendedVersion="
                + recommendedVersion + "]";
    }


}
