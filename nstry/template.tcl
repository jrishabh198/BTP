#Create a simulator object
set ns [new Simulator]
$ns rtproto DV
#Open the nam trace file
set nf [open out.nam w]
$ns namtrace-all $nf

#Define a 'finish' procedure
proc finish {} {
        global ns nf
        $ns flush-trace
	#Close the trace file
        close $nf
	#Execute nam on the trace file
        exec nam out.nam &
        exit 0
}

#Create two nodes
for {set i 0} {$i < 7} {incr i} {
  set n($i) [$ns node]
}

#Create a duplex link between the nodes
for {set i 0} {$i < 7} {incr i} {
  $ns duplex-link $n($i) $n([expr ($i+1)%7]) 1Mb 10ms DropTail
}

#Create a UDP agent and attach it to node n0



# Create a CBR traffic source and attach it to udp0
#Create a UDP agent and attach it to node n(0)
set udp0 [new Agent/TCP]
$ns attach-agent $n(0) $udp0

# Create a CBR traffic source and attach it to udp0
set cbr0 [new Application/Traffic/CBR]
$cbr0 set packetSize_ 500
$cbr0 set interval_ 0.005
$cbr0 attach-agent $udp0

set null0 [new Agent/Null]
$ns attach-agent $n(3) $null0

$ns connect $udp0 $null0


$ns rtmodel-at 1.0 down $n(1) $n(2)
$ns rtmodel-at 2.0 up $n(1) $n(2)
$ns rtmodel-at 1.5 down $n(5) $n(4)
$ns rtmodel-at 2.0 up $n(5) $n(4)

#Call the finish procedure after 5 seconds of simulation time
$ns at 5.0 "finish"
$ns at 0.5 "$cbr0 start"
$ns at 4.5 "$cbr0 stop"
#Run the simulation
$ns run
