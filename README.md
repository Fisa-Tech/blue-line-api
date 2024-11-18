Introduction
------------

This project is developed by the fifth-year Computer Science class at **INSA Hauts-de-France** as part of the **La Ligne
Bleue** initiative, requested by the **Université Polytechnique Hauts-de-France**. The goal of this project is to
\[briefly describe the project's purpose\].

Project Structure
-----------------

### Branching Strategy

* **main**: Production branch. Automatically deployed to the **production** server.

* **develop**: Pre-production branch. Automatically deployed to the **pre-production** server.

### Workflows

#### Developing New Features

1. **Create a Feature Branch**

* Branch name: `feature/issue-number`.
* Branch off from `develop`.

2. **Development**

* Implement the new feature.

* Ensure code adheres to project standards and passes all tests.

3. **Open a Pull Request (PR) to `develop`**

* Use the provided PR template.

* Include the issue number and a clear description.

4. **Code Review**

* Another team member reviews the PR.

* Address any feedback or requested changes.

5. **Merge into `develop`**

* Once approved, merge the PR without squashing commits.

* This preserves commit messages for **semantic-release** versioning.

6. **Pre-Production Validation**

* Verify the feature works correctly on the pre-production server.

#### Fixes

1. **Create a Fix Branch**

* Branch name: `fix/issue-number` or `fix/short-description`.

* Branch off from `develop`.

2. **Development**

* Implement the fix.

* Run tests to ensure stability.

3. **Rebase onto Latest `develop`**

* Before opening a PR, rebase your branch to the latest `develop` to avoid conflicts.

4. **Open a PR to `develop`**

* Follow the same process as feature branches.

5. **Merge into `develop`**

* After approval, merge without squashing commits.

#### Hotfixes

1. **Create a Hotfix Branch**

* Branch name: `hotfix/short-description`.

* Branch off from `main`.

2. **Development**

* Implement the critical fix needed in production.

3. **Open a PR to `main`**

* Use the PR template and get a quick review.

4. **Merge and Deploy**

* Merge into `main` without squashing commits.

* The hotfix is automatically deployed to production.

5. **Merge into `develop`**

* To keep branches in sync, merge the hotfix into `develop`.

Commit Message Guidelines
-------------------------

We adhere to the **Conventional Commits** specification and use **gitmoji** for commit messages.

### Commit Message Format

* **Start with an appropriate emoji** from the gitmoji convention that matches the content of the commit.

* **Do not use** `:emoji\_name:`; insert the actual emoji character.

* **First Line**: A short, imperative sentence describing the change (max 50 characters).

* **Second Line**: Leave empty.

* **Third Line Onwards**: A more detailed description, avoiding repetition and unnecessary details. Write 2-3 sentences
  at most.

### IntelliJ Commit Prompt

To streamline writing commit messages in IntelliJ IDEA, you can use the following prompt:

```
Start with an emoji from the Gitmoji convention that matches the content of the commit. Use the actual emoji character, not :name_of_the_emoji:.
Follow the Conventional Commits specification:
- Format: <emoji> <type>(optional scope): short description
- Types: feat, fix, docs, style, refactor, perf, test, build, ci, chore, revert
Start with a short imperative sentence, no more than 50 characters long.
Leave an empty line after the first line.
Provide a more detailed explanation in two or three sentences at most. Avoid overly verbose descriptions or unnecessary details. Be careful not to repeat the same information.
Do not describe the impact or consequences of the changes.
Only describe the changes made, do not analyze them.
```

Pull Request Template
---------------------

* Always use the provided PR template when creating a pull request.

* The template is automatically recognized by GitHub.

* Ensure all sections are filled out appropriately.

API Documentation
-----------------

* API documentation is available via Swagger UI.

* Access it at: `swagger-ui/index.html`

Deployment
----------

* **main Branch**: Merges trigger automatic deployment to the production server.

* **develop Branch**: Merges trigger automatic deployment to the pre-production server.

Best Practices
--------------

* **Rebasing**

* Always rebase your feature or fix branches onto the latest `develop` before opening a PR.

* This minimizes merge conflicts and ensures smooth integration.

* **No Squash Merging**

* Do not squash commits when merging PRs.

* Commit history is used by **semantic-release** for automated versioning.

* **Clear Commit Messages**

* Follow the commit message guidelines strictly.

* This helps in generating accurate changelogs and maintaining project clarity.

Conclusion
----------

By following these guidelines, we ensure efficient collaboration, maintain high code quality, and deliver reliable
software. If you have any questions or suggestions for improvement, please reach out to the project leads.

**Thank you for your contribution to the La Ligne Bleue project!**