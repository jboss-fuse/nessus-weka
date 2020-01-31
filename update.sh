#!/bin/bash

VERSION=1.0.2-SNAPSHOT

rm -rf index.html img
cp docs/target/nessus-weka-docs-$VERSION-userguide.tar.gz .
tar xzf nessus-weka-docs-$VERSION-userguide.tar.gz 
rm nessus-weka-docs-$VERSION-userguide.tar.gz 
git add --all
