/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2016. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

/**
 * DigitalSource Interface. The DigitalSource represents all the possible inputs
 * for a counter or a quadrature encoder. The source may be either a digital
 * input or an analog input. If the caller just provides a channel, then a
 * digital input will be constructed and freed when finished for the source. The
 * source can either be a digital input or analog trigger but not both.
 */
public abstract class DigitalSource {

  protected long m_port;
  protected int m_channel;

  protected void initDigitalPort(int channel, boolean input) {

  }

  /**
   * Get the channel routing number
   *
   * @return channel routing number
   */
  public int getChannelForRouting() {
    return m_channel;
  }

  /**
   * Get the module routing number
   *
   * @return 0
   */
  public byte getModuleForRouting() {
    return 0;
  }

  /**
   * Is this an analog trigger
   *$
   * @return true if this is an analog trigger
   */
  public boolean getAnalogTriggerForRouting() {
    return false;
  }
}
