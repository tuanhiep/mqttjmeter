#!/bin/bash
pid_cpu=$(cat pid_cpu.log)
kill $pid_cpu
pid_memo=$(cat pid_memo.log)
kill $pid_memo
pid_io=$(cat pid_io.log)
kill $pid_io
echo "Killed getmetrics process whose pid $pid_cpu $pid_memo $pid_io"
rm pid_cpu.log pid_memo.log pid_io.log
