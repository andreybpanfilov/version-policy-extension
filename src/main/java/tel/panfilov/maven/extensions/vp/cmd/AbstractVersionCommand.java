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

import org.semver4j.Semver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractVersionCommand implements VersionCommand {

    protected abstract Pattern getPattern();

    protected abstract Semver doApply(Matcher matcher, Semver version);

    @Override
    public boolean matches(String cmd) {
        return getPattern().matcher(cmd).matches();
    }

    @Override
    public Semver apply(String cmd, Semver version) {
        Matcher matcher = getPattern().matcher(cmd);
        if (matcher.matches()) {
            return doApply(matcher, version);
        } else {
            throw new IllegalArgumentException("no match");
        }
    }

}
