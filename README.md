# TeamCity GitHub Last Matching Ref Recipe

This TeamCity recipe fetches the latest GitHub reference (branch or tag) that matches a given pattern — such as `tags/v1` or `heads/feature`. The result is stored in a configurable environment variable and can be used throughout your build pipeline.

Ideal for release pipelines, dynamic versioning, or selecting the latest relevant code state from GitHub.

---

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="docs/images/teamcity-certified-partner-white.svg">
  <img src="docs/images/teamcity-certified-partner-black.svg" alt="TeamCity Certified Partner">
</picture>

## TeamCity Recipes & Plugins, YouTrack Apps & more

### J&J Ideenschmiede GmbH - Your Partner for JetBrains Solutions

We are **TeamCity Certified** and **YouTrack Certified** developers, specializing in custom solutions for JetBrains products.

👉🏼 Visit our [website](https://www.jj-ideenschmiede.de) to learn more about our services.

---

## Requirements

You need to have TeamCity 2025.03 or later installed to use this recipe. If you are using an older version than 2025.07, you need to upload the `github-last-matching-ref.yml`, which contains the complete recipe. If you are using TeamCity 2025.07 or later, you can use the `jjideenschmiede/github-last-matching-ref` recipe directly from the JetBrains Marketplace.

## Build

If you want to build this recipe, you can use the following command:

```bash
gradle recipe
```

## Help & Support

If you have any questions or need support, feel free to reach out to us via [e-mail](mailto:support@jj-ideenschmiede.de) or visit our [website](https://www.jj-ideenschmiede.de).
