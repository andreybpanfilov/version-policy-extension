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

package tel.panfilov.maven.extensions.vp.cmd;

import org.codehaus.plexus.component.annotations.Component;
import org.semver4j.Semver;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(role = VersionCommand.class, hint = "IncrementVersion")
public class IncrementVersionCommand extends AbstractVersionCommand {

    private static final Pattern INC_PATTERN = Pattern.compile("INC_(?<mmp>MINOR|MAJOR|PATCH)(_(?<inc>\\d+))?", Pattern.CASE_INSENSITIVE);

    @Override
    protected Pattern getPattern() {
        return INC_PATTERN;
    }

    @Override
    protected Semver doApply(Matcher matcher, Semver version) {
        String mmp = matcher.group("mmp");
        int inc = Optional.ofNullable(matcher.group("inc"))
                .map(Integer::parseInt)
                .orElse(1);
        if ("PATCH".equalsIgnoreCase(mmp)) {
            return version.withIncPatch(inc);
        }
        if ("MINOR".equalsIgnoreCase(mmp)) {
            return version.withIncMinor(inc)
                    .withIncPatch(-version.getPatch());
        }
        if ("MAJOR".equalsIgnoreCase(mmp)) {
            return version.withIncMajor(inc)
                    .withIncMinor(-version.getMinor())
                    .withIncPatch(-version.getPatch());
        }
        return version.withIncPatch();
    }
}
