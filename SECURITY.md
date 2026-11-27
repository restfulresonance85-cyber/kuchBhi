# Security Policy

## Supported Versions

| Version | Supported |
| ------- | --------- |
| `0.1.x` | Yes       |

## Mobile Security Standards Enforced in KuchBhi

- **HTTPS-Only Production Traffic:** `network_security_config.xml` disables cleartext HTTP traffic in `staging` and `prod` builds.
- **No Hardcoded Secrets:** API keys, signing keystores, and credentials must never be committed. Use CI secrets or `local.properties` (git-ignored).
- **Backup Disabled by Default:** `android:allowBackup="false"` prevents unencrypted local database extraction.
- **R8 Code Shrinking & Obfuscation:** Enabled in `release` builds with debug log stripping in `app/proguard-rules.pro`.

## Reporting a Vulnerability

Please do **not** open a public GitHub issue for security vulnerabilities. Report vulnerabilities privately to the maintainers with reproduction details and affected build variants.
