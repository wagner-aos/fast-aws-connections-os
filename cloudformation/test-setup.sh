#!/bin/bash
clear

. aws-session-token.sh dev

echo "Creating or Updating Cloud Formation Stack..."
python create_or_update_stack.py FastAWSConnectionsTeste fac-template.yml parameters.json dev

echo "Executing Tests..."
cd ..
cd fast-aws-connections

mvn install -DskipTests=false

cd ..
cd cloudformation

echo "end of test setup!"



