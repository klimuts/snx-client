# Contribution guidelines

## General terms

First of all, thanks for thinking of contributing to this project.

Before sending a Pull Request, please make sure that you're assigned the task on a GitHub issue.

- If a relevant issue already exists, discuss on the issue and get it assigned to yourself on GitHub.
- If no relevant issue exists, open a new issue and get it assigned to yourself on GitHub.

Please proceed with a Pull Request only after you're assigned. It'd be sad if your Pull Request (and your hardwork) isn't accepted just because it isn't ideologically compatible.  


## Development

### IDE run/debug

TODO

### Build distributive

To build distributive archive go to `/deploy` dir  
Run script `build.sh`, for example, from terminal:  
`$ bash build.sh`

This will create a directory `/build/distribution`, where the ZIP
`snx-client-v1.x.x.zip` will be placed there.

### Bump release
Note! For administrators only

To bump release go to `/deploy` dir  
Run script `up_version.sh --[major/minor/build]`, for example, from terminal:  
`$ bash up_version.sh --build`

This will increment the selected part of version and put zeros on the subsequent parts and create the appropriate git tag, for example:

```
major v1.39.3 -> v2.0.0  
minor v1.39.3 -> v1.40.0  
build v1.39.3 -> v1.39.4  
```
