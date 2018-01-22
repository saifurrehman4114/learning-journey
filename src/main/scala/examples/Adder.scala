// See LICENSE.txt for license details.
// January 22nd, 2018	- Adapting to Learning Journey
package examples

import Chisel._

//A n-bit adder with carry in and carry out
class Adder(n: Int) extends Module { 
  val io = new Bundle { 
    val A    = UInt(INPUT, n) 
    val B    = UInt(INPUT, n) 
    val Cin  = UInt(INPUT, 1) 
    val Sum  = UInt(OUTPUT, n) 
    val Cout = UInt(OUTPUT, 1) 
  } 
  // create a vector of FullAdders 
  val FAs = Vec.fill(n){ Module(new FullAdder()).io } 
 
  // define carry and sum wires 
  val carry = Vec.fill(n+1){ UInt(width = 1) } 
  val sum   = Vec.fill(n){ Bool() } 
 
  // first carry is the top level carry in 
  carry(0) := io.Cin 
 
  // wire up the ports of the full adders 
  for(i <- 0 until n) { 
     FAs(i).a   := io.A(i) 
     FAs(i).b   := io.B(i) 
     FAs(i).cin := carry(i) 
     carry(i+1) := FAs(i).cout 
     sum(i)     := FAs(i).sum.toBool() 
  } 
  io.Sum  := sum.toBits().toUInt() 
  io.Cout := carry(n) 
}
