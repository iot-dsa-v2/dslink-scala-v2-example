package org.iot.dsa.dslink.example

import org.iot.dsa.DSRuntime
import org.iot.dsa.dslink.DSMainNode
import org.iot.dsa.node.action.{ActionInvocation, ActionResult, DSAction}
import org.iot.dsa.node.{DSINumber, DSInfo, DSInt, DSString}

import scala.util.Try

/**
  * Created by Daniel on 4/11/2019.
  */
class MainNode extends DSMainNode with Runnable {
  ///////////////////////////////////////////////////////////////////////////
  // Constructors
  ///////////////////////////////////////////////////////////////////////////
  // Nodes must support the public no-arg constructor.


  ///////////////////////////////////////////////////////////////////////////
  // Class Fields
  ///////////////////////////////////////////////////////////////////////////
  private val COUNTER = "Counter"
  private val RESET = "Reset"
  private val WRITABLE = "Writable"

  ///////////////////////////////////////////////////////////////////////////
  // Instance Fields
  ///////////////////////////////////////////////////////////////////////////

  // Nodes store children and meta-data about the relationship in DSInfo instances.
  // Storing infos as Java fields eliminates subsequent name lookups, but should only be
  // done with declared defaults.  It can be done with dynamic children, but extra
  // care will be required.
  private val counter = getInfo(COUNTER)
  private val reset = getInfo(RESET)
  private var timer: Option[DSRuntime.Timer] = None


  ///////////////////////////////////////////////////////////////////////////
  // Public Methods
  ///////////////////////////////////////////////////////////////////////////

  /**
    * Handles the reset action.
    */
  override def invoke(action: DSInfo, target: DSInfo, invocation: ActionInvocation): ActionResult = {
    if (action == reset) {
      counter.synchronized {
        put(counter, DSInt.valueOf(0))
      }
      null
    } else {
      super.invoke(action, target, invocation)
    }
  }

  /**
    * Called by the timer, increments the counter.
    */
  override def run(): Unit = counter.synchronized {
    val value: DSINumber = counter.getValue.asInstanceOf[DSINumber]
    put(counter, DSInt.valueOf(value.toInt + 1))
  }

  ///////////////////////////////////////////////////////////////////////////
  // Protected Methods
  ///////////////////////////////////////////////////////////////////////////

  /**
    * Defines the permanent children of this node type, their existence is guaranteed in all
    * instances.  This is only ever called once per, type per process.
    */
  override def declareDefaults(): Unit = {
    super.declareDefaults()
    declareDefault(COUNTER, DSInt.valueOf(0)).setAdmin(true).setTransient(true).setReadOnly(true)
    declareDefault(WRITABLE, DSInt.valueOf(0))
    declareDefault(RESET, DSAction.DEFAULT)
    // Change the following URL to your README
    declareDefault("Help", DSString.valueOf("https://github.com/iot-dsa-v2/dslink-scala-v2-example")).setTransient(true).setReadOnly(true)
  }

  /**
    * Cancels an active timer if there is one.
    */
  override def onStopped(): Unit = timer match {
    case Some(t) =>
      t.cancel()
      timer = None
    case None => ()
  }

  /**
    * Starts the timer.
    */
  override def onSubscribed(): Unit = {
    timer = Some(DSRuntime.run(this, System.currentTimeMillis + 1000, 1000))
  }


  /**
    * Cancels the timer.
    */
  override def onUnsubscribed(): Unit = timer match {
    case Some(t) =>
      t.cancel()
      timer = None
    case None => ()
  }

}
