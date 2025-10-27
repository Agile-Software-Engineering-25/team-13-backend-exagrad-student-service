#!/bin/bash
# Quick format only (no checks)

echo "🔧 Applying Spotless formatting..."
./mvnw spotless:apply
echo "✨ Formatting complete!"
