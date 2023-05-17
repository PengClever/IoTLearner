package org.example;

public class Alphabet {
    String OpName, Source, Dest, Channel, Message, encryptSymbol;
    public Alphabet(String alphabet) {
        switch (alphabet) {
            case "ADU1CWRD88:97:46:2C:9A:CE":
                OpName = "AD";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "D88:97:46:2C:9A:CE";
                encryptSymbol = "2AD2U11C2WR18D88:97:46:2C:9A:CE";
                break;
            case "SAU1CWRU2":
                OpName = "SA";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "U2";
                encryptSymbol = "2SA2U11C2WR2U2";
                break;
            case "USU1CWRU2":
                OpName = "US";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "U2";
                encryptSymbol = "2US2U11C2WR2U2";
                break;
            case "DCU1CWRON":
                OpName = "DC";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "ON";
                encryptSymbol = "2DC2U11C2WR2ON";
                break;
            case "DCU1CWROF":
                OpName = "DC";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "OF";
                encryptSymbol = "2DC2U11C2WR2OF";
                break;
            case "DCU2CWRON":
                OpName = "DC";
                Source = "U2";
                Dest = "C";
                Channel = "WR";
                Message = "ON";
                encryptSymbol = "2DC2U21C2WR2ON";
                break;
            case "DCU2CWROF":
                OpName = "DC";
                Source = "U2";
                Dest = "C";
                Channel = "WR";
                Message = "OF";
                encryptSymbol = "2DC2U21C2WR2OF";
                break;
            case "DCU1D88:97:46:2C:9A:CEWLON":
                OpName = "DC";
                Source = "U1";
                Dest = "D88:97:46:2C:9A:CE";
                Channel = "WL";
                Message = "ON";
                encryptSymbol = "2DC2U118D88:97:46:2C:9A:CE2WL2ON";
                break;
            case "DCU1D88:97:46:2C:9A:CEWLOF":
                OpName = "DC";
                Source = "U1";
                Dest = "D88:97:46:2C:9A:CE";
                Channel = "WL";
                Message = "OF";
                encryptSymbol = "2DC2U118D88:97:46:2C:9A:CE2WL2OF";
                break;
            case "DCU2D88:97:46:2C:9A:CEWLON":
                OpName = "DC";
                Source = "U2";
                Dest = "D88:97:46:2C:9A:CE";
                Channel = "WL";
                Message = "ON";
                encryptSymbol = "2DC2U218D88:97:46:2C:9A:CE2WL2ON";
                break;
            case "DCU2D88:97:46:2C:9A:CEWLOF":
                OpName = "DC";
                Source = "U2";
                Dest = "D88:97:46:2C:9A:CE";
                Channel = "WL";
                Message = "OF";
                encryptSymbol = "2DC2U218D88:97:46:2C:9A:CE2WL2OF";
                break;
            case "DDU1CWRD88:97:46:2C:9A:CE":
                OpName = "DD";
                Source = "U1";
                Dest = "C";
                Channel = "WR";
                Message = "D88:97:46:2C:9A:CE";
                encryptSymbol = "2DD2U11C2WR18D88:97:46:2C:9A:CE";
                break;
            case "IRU2CWRAC":
                OpName = "IR";
                Source = "U2";
                Dest = "C";
                Channel = "WR";
                Message = "AC";
                encryptSymbol = "2IR2U21C2WR2AC";
                break;
            case "IRU2CWRDE":
                OpName = "IR";
                Source = "U2";
                Dest = "C";
                Channel = "WR";
                Message = "DE";
                encryptSymbol = "2IR2U21C2WR2DE";
                break;
            case "RESET":
                OpName = "RESET";
                Source = "";
                Dest = "";
                Channel = "";
                Message = "";
                encryptSymbol = "5RESET0000";
                break;
            case "FINISH":
                OpName = "FINISH";
                Source = "";
                Dest = "";
                Channel = "";
                Message = "";
                encryptSymbol = "6FINISH0000";
                break;
            default:
                OpName = "";
                Source = "";
                Dest = "";
                Channel = "";
                Message = "";
                encryptSymbol = "Wrong_symbol";
        }
    }
}
