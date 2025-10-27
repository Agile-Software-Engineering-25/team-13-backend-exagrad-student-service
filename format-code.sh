#!/bin/bash
# Complete code quality check: format + validate

echo "🔧 Applying Spotless formatting..."
./mvnw spotless:apply

echo "✅ Checking Spotless compliance..."
./mvnw spotless:check

echo "🔍 Running Checkstyle analysis..."
./mvnw checkstyle:check

echo "✨ All checks complete!"
