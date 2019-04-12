package org.iot.dsa.dslink.example

import org.iot.dsa.dslink.DSMainNode
import org.iot.dsa.node.DSInt

/**
  * Created by Daniel on 4/11/2019.
  */
class MainNode extends DSMainNode {

  override def declareDefaults(): Unit = {
    super.declareDefaults()
    declareDefault("Number", DSInt.valueOf(42))
  }

}
