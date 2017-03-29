package com.ctre;

public enum CTR_Code
{
  CTR_OKAY(0), 
  CTR_RxTimeout(1), 
  CTR_TxTimeout(2), 
  CTR_InvalidParamValue(3), 
  CTR_UnexpectedArbId(4), 
  CTR_TxFailed(5), 
  CTR_SigNotUpdated(6), 
  CTR_BufferFull(7), 
  CTR_UnknownError(8);
  
  private CTR_Code(int value) { this.value = value; }
  
  public static CTR_Code getEnum(int value) { for (CTR_Code e : CTR_Code.values()) {
      if (value == e.value) {
        return e;
      }
    }
    return CTR_UnknownError; }
  
  public int IntValue() { return value; }
  
  private int value;
}
