#!/usr/bin/env bash
./gradlew :strict-mode-compat:assembleRelease :strict-mode-compat-kotlin:assembleRelease :strict-mode-compat:bintrayUpload :strict-mode-compat-kotlin:bintrayUpload -PdisablePreDex
