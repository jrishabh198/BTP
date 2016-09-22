set ns [new Simulator]

set f [open out.nam w]

$ns namtrace-all $f

proc end {} {
	global ns f
	$ns flush-trace
	close $f

	exec nam out.nam &
	exit 0
}

set n0 [$ns node]
set n1 [$ns node]

$ns duplex-link $n0 $n1 1mb 50ms DropTail
$ns duplex-link-op $n0 $n1 orient right


set tcp [new Agent/TCP]
$ns attach-agent $n0 $tcp

set tcpsink [new Agent/TCPSink]
$ns attach-agent $n1 $tcpsink

$ns connect $tcp $tcpsink

set ftp [new Application/FTP]
$ftp attach-agent $tcp

$ns at 1 "$ftp start"

$ns at 3 end
$ns run