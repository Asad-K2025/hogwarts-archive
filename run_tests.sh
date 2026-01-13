#!/bin/bash

javac *.java
if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

TEST_DIR="./tests"
pass_count=0
fail_count=0

for input_file in "$TEST_DIR"/*.in; do
    base_name=$(basename "$input_file" .in)
    output_file="$TEST_DIR/$base_name.out"
    temp_output="temp_$base_name.out"

    java Archive < "$input_file" > "$temp_output"

    if diff -q "$temp_output" "$output_file" > /dev/null; then
        ((pass_count++))
    else
        ((fail_count++))
        echo "[FAIL] $base_name"
        diff "$temp_output" "$output_file"
        echo ""
    fi

    rm "$temp_output"
done

echo "=============================="
echo "Passed: $pass_count"
echo "Failed: $fail_count"
echo "=============================="

if [ $fail_count -ne 0 ]; then
    exit 1
fi
