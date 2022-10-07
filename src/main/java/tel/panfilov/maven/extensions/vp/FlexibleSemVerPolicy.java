/*-
 * #%L
 * version-policy-extension
 * %%
 * Copyright (C) 2022 Project Contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package tel.panfilov.maven.extensions.vp;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.shared.release.policy.PolicyException;
import org.apache.maven.shared.release.policy.version.VersionPolicy;
import org.apache.maven.shared.release.policy.version.VersionPolicyRequest;
import org.apache.maven.shared.release.policy.version.VersionPolicyResult;
import org.apache.maven.shared.release.versions.VersionParseException;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.semver4j.Semver;
import tel.panfilov.maven.extensions.vp.cmd.VersionCommand;

import java.util.List;
import java.util.Properties;

@Component(role = VersionPolicy.class, hint = "FlexibleSemVerPolicy", description = "Flexible VersionPolicy following the SemVer rules")
public class FlexibleSemVerPolicy implements VersionPolicy {

    @Requirement
    private MavenSession session;

    @Requirement(role = VersionCommand.class)
    private List<VersionCommand> commands;

    @Override
    public VersionPolicyResult getReleaseVersion(VersionPolicyRequest request) throws PolicyException, VersionParseException {
        Semver version = getReleaseVersion(request.getVersion());
        return new VersionPolicyResult().setVersion(version.getVersion());
    }

    protected Semver getReleaseVersion(String requestVersion) {
        Semver version = new Semver(requestVersion)
                .withClearedPreReleaseAndBuild();
        String cmd = getNextVersionCommand("R");
        return commands.stream()
                .filter(c -> c.matches(cmd))
                .findFirst()
                .map(c -> c.apply(cmd, version))
                .orElse(version);
    }

    @Override
    public VersionPolicyResult getDevelopmentVersion(VersionPolicyRequest request) throws PolicyException, VersionParseException {
        Semver version = new Semver(request.getVersion());
        List<String> preRelease = version.getPreRelease();
        List<String> build = version.getBuild();
        Semver dev;
        if (preRelease.isEmpty() && build.isEmpty() || preRelease.contains("SNAPSHOT")) {
            String cmd = getNextVersionCommand("D");
            dev = commands.stream()
                    .filter(c -> c.matches(cmd))
                    .findFirst()
                    .map(c -> c.apply(cmd, version))
                    .orElse(version.withIncPatch());
        } else {
            dev = version.withClearedPreReleaseAndBuild();
        }
        dev = dev.withPreRelease("SNAPSHOT");
        return new VersionPolicyResult().setVersion(dev.getVersion());
    }

    protected String getNextVersionCommand(String suffix) {
        Properties properties = session.getUserProperties();
        String property = "fsvp" + suffix.toLowerCase();
        String cmd = properties.getProperty(property);
        if (cmd == null) {
            cmd = System.getenv(property.toUpperCase());
        }
        return cmd;
    }


}
