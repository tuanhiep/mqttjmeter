#!/bin/bash

sar -u 1 > cpuload.log &
pid_cpu=$!
echo $pid_cpu > pid_cpu.log
sar -r 1 > memo.log &
pid_memo=$!
echo $pid_memo > pid_memo.log
sar -b 1 > io.log &
pid_io=$!
echo $pid_io > pid_io.log
echo "Getting metrics with process id : $pid_cpu $pid_memo $pid_io"

