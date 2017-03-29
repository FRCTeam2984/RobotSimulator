package com.ctre;

public interface GadgeteerUartClient {
  public abstract int GetGadgeteerStatus(GadgeteerUartStatus paramGadgeteerUartStatus);
  
  public static enum GadgeteerProxyType { General(0), 
    Pigeon(1), 
    PC_HERO(2), 
    Unknown(-1);
    private GadgeteerProxyType(int value) {}
    
    public static GadgeteerProxyType getEnum(int value) { 
      return Unknown;
    }
    
    }
  
  public static enum GadgeteerConnection { NotConnected(0), 
    Connecting(1), 
    Connected(2), 
    Unknown(-1);
    private GadgeteerConnection(int value) { }
    
    public static GadgeteerConnection getEnum(int value) {
      return Unknown;
    }
    
  }
  
  public static class GadgeteerUartStatus { public GadgeteerUartClient.GadgeteerProxyType type;
    public GadgeteerUartClient.GadgeteerConnection conn;
    public int bitrate;
    public int resetCount;
    
    public GadgeteerUartStatus() {}
  }
  
  public static enum GadgeteerState { GadState_WaitChirp1(0), 
    GadState_WaitBLInfo(1), 
    GadState_WaitBitrateResp(2), 
    GadState_WaitSwitchDelay(3), 
    GadState_WaitChirp2(4), 
    GadState_Connected_Idle(5), 
    GadState_Connected_ReqChirp(6), 
    GadState_Connected_RespChirp(7), 
    GadState_Connected_ReqCanBus(8), 
    GadState_Connected_RespCanBus(9), 
    GadState_Connected_RespIsoThenChirp(10), 
    GadState_Connected_RespIsoThenCanBus(11);
    private GadgeteerState(int value) { }
    
    public static GadgeteerState getEnum(int value) { 
      return GadState_WaitChirp1;
    }
    
  }
}
