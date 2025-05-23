/**
 * @(#)FileManager.java 2019/12/01
 *
 * ICE Team free software group
 *
 * This file is part of C64 Java Software Emulator.
 * See README for copyright notice.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *  02111-1307  USA.
 */
package sw_emulator.swing.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import sw_emulator.software.Assembler;
import sw_emulator.software.Assembler.Name;
import sw_emulator.software.BasicDetokenize.BasicType;
import sw_emulator.software.MemoryDasm;

/**
 * Manage the files of disassembler
 * 
 * @author ice
 */
public class FileManager {
  /** Public access to file manager */
  public static final FileManager instance=new FileManager();
    
  /** File to use for option*/
  public static final File OPTION_FILE=new File(System.getProperty("user.home")+File.separator+".jc64dis");
  
  /** Header for costant file */
  private static final String HEADER_CST="CST";
    
  /**
   * Singleton contructor
   */
  private FileManager() {
  }
    
  /**
   * Read option file
   * If file is not present, operation is ok
   * 
   * @param file the file to use
   * @param option the option to fill
   * @return true if operation is ok
   */
  public boolean readOptionFile(File file, Option option) {
    try {      
      DataInputStream in=new DataInputStream(
                         new BufferedInputStream(
                         new FileInputStream(file))); 
     
      option.opcodeUpperCasePreview = in.readBoolean();
      option.opcodeUpperCaseSource = in.readBoolean();
      option.illegalOpcodeMode = in.readByte();
      option.commentLanguage = in.readByte();     
      option.useAsCode = in.readBoolean();
      option.eraseDComm = in.readBoolean();
      option.erasePlus = in.readBoolean();
      option.maxLabelLength = in.readInt();
      option.maxByteAggregate = in.readInt();
      
      option.psidInitSongsLabel = in.readUTF();
      option.psidPlaySoundsLabel = in.readUTF();
      option.sidFreqLoLabel = in.readUTF();
      option.sidFreqHiLabel = in.readUTF();
      
      // 0.8     
      option.theme = in.readInt();
      option.lafName = in.readUTF();
      option.flatLaf = in.readUTF();
      
      // 0.9
      option.numInstrSpaces = in.readInt();
      option.numInstrTabs = in.readInt();
      option.numDataSpaces = in.readInt();
      option.numDataTabs = in.readInt();
      option.labelOnSepLine = in.readBoolean();
      option.clickDcErase = in.readBoolean();
      option.clickDlErase = in.readBoolean();
      option.clickUbEdit = in.readBoolean();
      option.clickUcEdit = in.readBoolean();
      option.clickUlEdit = in.readBoolean();
      option.forceCompilation = in.readBoolean();
      
      option.maxWordAggregate = in.readInt();
      option.maxTribyteAggregate = in.readInt();
      option.maxLongAggregate = in.readInt();
      option.maxSwappedAggregate = in.readInt();
      option.maxTextAggregate = in.readInt();
      option.maxAddressAggregate = in.readInt();
      option.maxStackWordAggregate = in.readInt();
      option.tmpPath = in.readUTF();
     
      option.dasmF3Comp = in.readBoolean();
      option.dasmStarting = Assembler.Starting.valueOf(in.readUTF());
      option.dasmOrigin = Assembler.Origin.valueOf(in.readUTF());
      option.dasmLabel = Assembler.Label.valueOf(in.readUTF());
      option.dasmComment = Assembler.Comment.valueOf(in.readUTF());
      option.dasmBlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.dasmByte = Assembler.Byte.valueOf(in.readUTF());
      option.dasmWord = Assembler.Word.valueOf(in.readUTF());
      option.dasmWordSwapped = Assembler.WordSwapped.valueOf(in.readUTF());
      option.dasmTribyte = Assembler.Tribyte.valueOf(in.readUTF()); 
      option.dasmLong = Assembler.Long.valueOf(in.readUTF());
      option.dasmAddress = Assembler.Address.valueOf(in.readUTF());
      option.dasmStackWord = Assembler.StackWord.valueOf(in.readUTF());
      option.dasmMonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.dasmMultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());
      option.dasmText = Assembler.Text.valueOf(in.readUTF());
      option.dasmNumText = Assembler.NumText.valueOf(in.readUTF());
      option.dasmZeroText = Assembler.ZeroText.valueOf(in.readUTF());
      option.dasmHighText = Assembler.HighText.valueOf(in.readUTF());
      option.dasmShiftText = Assembler.ShiftText.valueOf(in.readUTF());
      option.dasmScreenText = Assembler.ScreenText.valueOf(in.readUTF());
      option.dasmPetasciiText = Assembler.PetasciiText.valueOf(in.readUTF());
      
      option.tmpxStarting = Assembler.Starting.valueOf(in.readUTF());
      option.tmpxOrigin = Assembler.Origin.valueOf(in.readUTF());
      option.tmpxLabel = Assembler.Label.valueOf(in.readUTF());
      option.tmpxComment = Assembler.Comment.valueOf(in.readUTF());
      option.tmpxBlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.tmpxByte = Assembler.Byte.valueOf(in.readUTF());
      option.tmpxWord = Assembler.Word.valueOf(in.readUTF());
      option.tmpxWordSwapped = Assembler.WordSwapped.valueOf(in.readUTF());
      option.tmpxTribyte = Assembler.Tribyte.valueOf(in.readUTF());
      option.tmpxLong = Assembler.Long.valueOf(in.readUTF());
      option.tmpxAddress = Assembler.Address.valueOf(in.readUTF());
      option.tmpxStackWord = Assembler.StackWord.valueOf(in.readUTF());
      option.tmpxMonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.tmpxMultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());
      option.tmpxText = Assembler.Text.valueOf(in.readUTF());
      option.tmpxNumText = Assembler.NumText.valueOf(in.readUTF());
      option.tmpxZeroText = Assembler.ZeroText.valueOf(in.readUTF());
      option.tmpxHighText = Assembler.HighText.valueOf(in.readUTF());
      option.tmpxShiftText = Assembler.ShiftText.valueOf(in.readUTF());
      option.tmpxScreenText = Assembler.ScreenText.valueOf(in.readUTF());   
      option.tmpxPetasciiText = Assembler.PetasciiText.valueOf(in.readUTF());
      
      option.ca65Starting = Assembler.Starting.valueOf(in.readUTF());
      option.ca65Origin = Assembler.Origin.valueOf(in.readUTF());
      option.ca65Label = Assembler.Label.valueOf(in.readUTF());
      option.ca65Comment = Assembler.Comment.valueOf(in.readUTF());
      option.ca65BlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.ca65Byte = Assembler.Byte.valueOf(in.readUTF());
      option.ca65Word = Assembler.Word.valueOf(in.readUTF()); 
      option.ca65WordSwapped = Assembler.WordSwapped.valueOf(in.readUTF());
      option.ca65Tribyte = Assembler.Tribyte.valueOf(in.readUTF());
      option.ca65Long = Assembler.Long.valueOf(in.readUTF());
      option.ca65Address = Assembler.Address.valueOf(in.readUTF());
      option.ca65StackWord = Assembler.StackWord.valueOf(in.readUTF());
      option.ca65MonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.ca65MultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());
      option.ca65Text = Assembler.Text.valueOf(in.readUTF());
      option.ca65NumText = Assembler.NumText.valueOf(in.readUTF());
      option.ca65ZeroText = Assembler.ZeroText.valueOf(in.readUTF());
      option.ca65HighText = Assembler.HighText.valueOf(in.readUTF());
      option.ca65ShiftText = Assembler.ShiftText.valueOf(in.readUTF());
      option.ca65ScreenText = Assembler.ScreenText.valueOf(in.readUTF()); 
      option.ca65PetasciiText = Assembler.PetasciiText.valueOf(in.readUTF());
 
      option.acmeStarting = Assembler.Starting.valueOf(in.readUTF());
      option.acmeOrigin = Assembler.Origin.valueOf(in.readUTF());
      option.acmeLabel = Assembler.Label.valueOf(in.readUTF());
      option.acmeComment = Assembler.Comment.valueOf(in.readUTF());
      option.acmeBlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.acmeByte = Assembler.Byte.valueOf(in.readUTF());
      option.acmeWord = Assembler.Word.valueOf(in.readUTF()); 
      option.acmeWordSwapped = Assembler.WordSwapped.valueOf(in.readUTF()); 
      option.acmeTribyte = Assembler.Tribyte.valueOf(in.readUTF());
      option.acmeLong = Assembler.Long.valueOf(in.readUTF());
      option.acmeAddress = Assembler.Address.valueOf(in.readUTF());
      option.acmeStackWord = Assembler.StackWord.valueOf(in.readUTF());
      option.acmeMonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.acmeMultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());
      option.acmeText = Assembler.Text.valueOf(in.readUTF());
      option.acmeNumText = Assembler.NumText.valueOf(in.readUTF());
      option.acmeZeroText = Assembler.ZeroText.valueOf(in.readUTF());
      option.acmeHighText = Assembler.HighText.valueOf(in.readUTF());
      option.acmeShiftText = Assembler.ShiftText.valueOf(in.readUTF());
      option.acmeScreenText = Assembler.ScreenText.valueOf(in.readUTF());  
      option.acmePetasciiText = Assembler.PetasciiText.valueOf(in.readUTF());
            
      option.kickColonMacro = in.readBoolean();
      option.kickStarting = Assembler.Starting.valueOf(in.readUTF());
      option.kickOrigin = Assembler.Origin.valueOf(in.readUTF());
      option.kickLabel = Assembler.Label.valueOf(in.readUTF());
      option.kickComment = Assembler.Comment.valueOf(in.readUTF());
      option.kickBlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.kickByte = Assembler.Byte.valueOf(in.readUTF());
      option.kickWord = Assembler.Word.valueOf(in.readUTF());   
      option.kickWordSwapped = Assembler.WordSwapped.valueOf(in.readUTF()); 
      option.kickTribyte = Assembler.Tribyte.valueOf(in.readUTF());
      option.kickLong = Assembler.Long.valueOf(in.readUTF());
      option.kickAddress = Assembler.Address.valueOf(in.readUTF());
      option.kickStackWord = Assembler.StackWord.valueOf(in.readUTF());
      option.kickMonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.kickMultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());
      option.kickText = Assembler.Text.valueOf(in.readUTF());
      option.kickNumText = Assembler.NumText.valueOf(in.readUTF());
      option.kickZeroText = Assembler.ZeroText.valueOf(in.readUTF());
      option.kickHighText = Assembler.HighText.valueOf(in.readUTF());
      option.kickShiftText = Assembler.ShiftText.valueOf(in.readUTF());
      option.kickScreenText = Assembler.ScreenText.valueOf(in.readUTF());
      option.kickPetasciiText = Assembler.PetasciiText.valueOf(in.readUTF());
            
      option.tass64Starting = Assembler.Starting.valueOf(in.readUTF());
      option.tass64Origin = Assembler.Origin.valueOf(in.readUTF());
      option.tass64Label = Assembler.Label.valueOf(in.readUTF());
      option.tass64Comment = Assembler.Comment.valueOf(in.readUTF());
      option.tass64BlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.tass64Byte = Assembler.Byte.valueOf(in.readUTF());
      option.tass64Word = Assembler.Word.valueOf(in.readUTF());
      option.tass64WordSwapped = Assembler.WordSwapped.valueOf(in.readUTF()); 
      option.tass64Tribyte = Assembler.Tribyte.valueOf(in.readUTF());
      option.tass64Long = Assembler.Long.valueOf(in.readUTF());
      option.tass64Address = Assembler.Address.valueOf(in.readUTF());
      option.tass64StackWord = Assembler.StackWord.valueOf(in.readUTF());
      option.tass64MonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.tass64MultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());      
      option.tass64Text = Assembler.Text.valueOf(in.readUTF());
      option.tass64NumText = Assembler.NumText.valueOf(in.readUTF());
      option.tass64ZeroText = Assembler.ZeroText.valueOf(in.readUTF()); 
      option.tass64HighText = Assembler.HighText.valueOf(in.readUTF()); 
      option.tass64ShiftText = Assembler.ShiftText.valueOf(in.readUTF());
      option.tass64ScreenText = Assembler.ScreenText.valueOf(in.readUTF()); 
      option.tass64PetasciiText = Assembler.PetasciiText.valueOf(in.readUTF());
      
      option.commentC64ZeroPage = in.readBoolean();
      option.commentC64StackArea = in.readBoolean();
      option.commentC64_200Area = in.readBoolean();
      option.commentC64_300Area = in.readBoolean();
      option.commentC64ScreenArea = in.readBoolean();
      option.commentC64BasicFreeArea = in.readBoolean();
      option.commentC64BasicRom = in.readBoolean();
      option.commentC64FreeRam = in.readBoolean();
      option.commentC64VicII = in.readBoolean();
      option.commentC64Sid = in.readBoolean();
      option.commentC64ColorArea = in.readBoolean();          
      option.commentC64Cia1 = in.readBoolean();
      option.commentC64Cia2 = in.readBoolean();
      option.commentC64KernalRom = in.readBoolean(); 
      
      option.commentC128ZeroPage = in.readBoolean();
      option.commentC128StackArea = in.readBoolean();
      option.commentC128_200Area = in.readBoolean(); 
      option.commentC128_300Area = in.readBoolean(); 
      option.commentC128ScreenArea = in.readBoolean(); 
      
      option.commentC128AppProgArea = in.readBoolean();  
      option.commentC128BasicRom = in.readBoolean();  
      option.commentC128Cia1 = in.readBoolean();
      option.commentC128Cia2 = in.readBoolean();
      option.commentC128Color = in.readBoolean();
      option.commentC128DMA = in.readBoolean();  
      option.commentC128KernalRom = in.readBoolean();
      option.commentC128MMU = in.readBoolean();
      option.commentC128ScreenMem = in.readBoolean();
      option.commentC128UserBasic = in.readBoolean();
      option.commentC128VDC = in.readBoolean();
      option.commentC128VideoColor = in.readBoolean();  
      option.commentC128VicII = in.readBoolean();
      option.commentC128Sid = in.readBoolean();
      
      option.commentC1541ZeroPage = in.readBoolean();
      option.commentC1541StackArea = in.readBoolean();
      option.commentC1541_200Area = in.readBoolean();  
      option.commentC1541Buffer0 = in.readBoolean();  
      option.commentC1541Buffer1 = in.readBoolean();  
      option.commentC1541Buffer2 = in.readBoolean();  
      option.commentC1541Buffer3 = in.readBoolean();  
      option.commentC1541Buffer4 = in.readBoolean();  
      option.commentC1541Via1 = in.readBoolean();  
      option.commentC1541Via2 = in.readBoolean();  
      option.commentC1541Kernal = in.readBoolean();  
      
      option.commentPlus4ZeroPage = in.readBoolean(); 
      option.commentPlus4StackArea = in.readBoolean(); 
      option.commentPlus4_200Area = in.readBoolean(); 
      option.commentPlus4_300Area = in.readBoolean(); 
      option.commentPlus4_400Area = in.readBoolean(); 
      option.commentPlus4_500Area = in.readBoolean(); 
      option.commentPlus4_600Area = in.readBoolean(); 
      option.commentPlus4_700Area = in.readBoolean(); 
      option.commentPlus4ColorArea = in.readBoolean(); 
      option.commentPlus4VideoArea = in.readBoolean(); 
      option.commentPlus4BasicRamP = in.readBoolean(); 
      option.commentPlus4BasicRamN = in.readBoolean(); 
      option.commentPlus4Luminance = in.readBoolean(); 
      option.commentPlus4ColorBitmap = in.readBoolean(); 
      option.commentPlus4GraphicData = in.readBoolean(); 
      option.commentPlus4BasicRom = in.readBoolean(); 
      option.commentPlus4BasicExt = in.readBoolean(); 
      option.commentPlus4Caracter = in.readBoolean(); 
      option.commentPlus4Acia = in.readBoolean(); 
      option.commentPlus4_6529B_1   = in.readBoolean(); 
      option.commentPlus4_6529B_2 = in.readBoolean(); 
      option.commentPlus4Ted = in.readBoolean(); 
      option.commentPlus4Kernal = in.readBoolean();     
      
      option.commentVic20ZeroPage = in.readBoolean();
      option.commentVic20StackArea = in.readBoolean();
      option.commentVic20_200Area = in.readBoolean();
      option.commentVic20_300Area = in.readBoolean();
      option.commentVic20_400Area = in.readBoolean();
      option.commentVic20Vic = in.readBoolean();
      option.commentVic20Via1 = in.readBoolean();
      option.commentVic20Via2 = in.readBoolean();
      option.commentVic20UserBasic = in.readBoolean();
      option.commentVic20Screen = in.readBoolean();
      option.commentVic20_8kExp1 = in.readBoolean();
      option.commentVic20_8kExp2 = in.readBoolean();
      option.commentVic20_8kExp3 = in.readBoolean();
      option.commentVic20Character = in.readBoolean();
      option.commentVic20Color = in.readBoolean();
      option.commentVic20Block2 = in.readBoolean();
      option.commentVic20Block3 = in.readBoolean();
      option.commentVic20Block4 = in.readBoolean();
      option.commentVic20BasicRom = in.readBoolean();
      option.commentVic20KernalRom = in.readBoolean();
                  
      option.assembler = Name.valueOf(in.readUTF());
      
      // 1.0 
      option.memoryValue = in.readByte();
      option.numInstrCSpaces = in.readInt();
      option.numInstrCTabs = in.readInt();
      option.numDataCSpaces = in.readInt();
      option.numDataCTabs = in.readInt();
      
      // 1.2
      option.allowUtf = in.readBoolean();
      
      // 1.6
      option.useSidFreq=in.readBoolean();
      option.sidFreqMarkMem=in.readBoolean();
      option.sidFreqCreateLabel=in.readBoolean();
      option.sidFreqCreateComment=in.readBoolean();
      option.createPSID=in.readBoolean();
      option.notMarkPSID=in.readBoolean();
      option.heather=in.readInt();
      option.custom=in.readUTF();
      option.repositionate=in.readBoolean();
      
      // 1.7
      option.pedantic=in.readBoolean();
      option.clickVlPatch=in.readBoolean();
      
      // 2.1
      option.glassStarting = Assembler.Starting.valueOf(in.readUTF());
      option.glassOrigin = Assembler.Origin.valueOf(in.readUTF());
      option.glassLabel = Assembler.Label.valueOf(in.readUTF());
      option.glassComment = Assembler.Comment.valueOf(in.readUTF());
      option.glassBlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.glassByte = Assembler.Byte.valueOf(in.readUTF());
      option.glassWord = Assembler.Word.valueOf(in.readUTF());
      option.glassWordSwapped = Assembler.WordSwapped.valueOf(in.readUTF()); 
      option.glassTribyte = Assembler.Tribyte.valueOf(in.readUTF());
      option.glassLong = Assembler.Long.valueOf(in.readUTF());
      option.glassAddress = Assembler.Address.valueOf(in.readUTF());
      option.glassStackWord = Assembler.StackWord.valueOf(in.readUTF());
      option.glassMonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.glassMultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());      
      option.glassText = Assembler.Text.valueOf(in.readUTF());
      option.glassNumText = Assembler.NumText.valueOf(in.readUTF());
      option.glassZeroText = Assembler.ZeroText.valueOf(in.readUTF()); 
      option.glassHighText = Assembler.HighText.valueOf(in.readUTF()); 
      option.glassShiftText = Assembler.ShiftText.valueOf(in.readUTF());
      option.glassScreenText = Assembler.ScreenText.valueOf(in.readUTF()); 
      option.glassPetasciiText = Assembler.PetasciiText.valueOf(in.readUTF());   
      
      //2.3
      option.numSpacesOp=in.readInt();
      option.numTabsOp=in.readInt();
      option.sizePreviewFont=in.readInt();
      option.sizeSourceFont=in.readInt();
      
      // 2.4
      option.createSAP=in.readBoolean();
      option.notMarkSAP=in.readBoolean();
      option.commentAtariZeroPage=in.readBoolean();
      option.commentAtariStackArea=in.readBoolean();
      option.commentAtari_200Area=in.readBoolean();
      option.commentAtari_300Area=in.readBoolean();
      option.commentAtari_400Area=in.readBoolean();
      option.commentAtari_500Area=in.readBoolean();
      option.commentAtariCartridgeB=in.readBoolean();
      option.commentAtariCartridgeA=in.readBoolean();
      option.commentAtariGtia=in.readBoolean();
      option.commentAtariPokey=in.readBoolean();
      option.commentAtariPia=in.readBoolean();
      option.commentAtariAntic=in.readBoolean();
      option.commentAtariKernalRom=in.readBoolean();    
      
      // 2.5
      option.dotsType=in.readInt();
      option.noUndocumented=in.readBoolean();
      
      // 2.6
      option.chooserPerc=in.readBoolean();
      option.showMiniature=in.readBoolean();
      
      // 2.7
      option.syntaxTheme=in.readUTF();
      
      // 2.8
      option.showSidId=in.readBoolean();
      option.sidIdPath=in.readUTF();
      
      // 2.9
      option.sidFreqLinearTable=in.readBoolean();
      option.sidFreqCombinedTable=in.readBoolean();
      option.sidFreqInverseLinearTable=in.readBoolean();
      option.sidFreqLinearOctNoteTable=in.readBoolean();
      option.sidFreqHiOct13Table=in.readBoolean();
      option.sidFreqLinearScaleTable=in.readBoolean();
      option.sidFreqShortLinearTable=in.readBoolean();
      option.sidFreqShortCombinedTable=in.readBoolean();
      option.sidFreqHiOctCombinedTable=in.readBoolean();
      option.sidFreqHiOctCombinedInvertedTable=in.readBoolean();
      option.sidFreqLoOctCombinedTable=in.readBoolean();
      option.sidFreqHiOct12Table=in.readBoolean();  
      
      // 3.0
      option.asmStarting = Assembler.Starting.valueOf(in.readUTF());
      option.asmOrigin = Assembler.Origin.valueOf(in.readUTF());
      option.asmLabel = Assembler.Label.valueOf(in.readUTF());
      option.asmComment = Assembler.Comment.valueOf(in.readUTF());
      option.asmBlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.asmByte = Assembler.Byte.valueOf(in.readUTF());
      option.asmWord = Assembler.Word.valueOf(in.readUTF());
      option.asmWordSwapped = Assembler.WordSwapped.valueOf(in.readUTF()); 
      option.asmTribyte = Assembler.Tribyte.valueOf(in.readUTF());
      option.asmLong = Assembler.Long.valueOf(in.readUTF());
      option.asmAddress = Assembler.Address.valueOf(in.readUTF());
      option.asmStackWord = Assembler.StackWord.valueOf(in.readUTF());
      option.asmMonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.asmMultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());      
      option.asmText = Assembler.Text.valueOf(in.readUTF());
      option.asmNumText = Assembler.NumText.valueOf(in.readUTF());
      option.asmZeroText = Assembler.ZeroText.valueOf(in.readUTF()); 
      option.asmHighText = Assembler.HighText.valueOf(in.readUTF()); 
      option.asmShiftText = Assembler.ShiftText.valueOf(in.readUTF());
      option.asmScreenText = Assembler.ScreenText.valueOf(in.readUTF()); 
      option.asmPetasciiText = Assembler.PetasciiText.valueOf(in.readUTF());         
      
      option.asiStarting = Assembler.Starting.valueOf(in.readUTF());
      option.asiOrigin = Assembler.Origin.valueOf(in.readUTF());
      option.asiLabel = Assembler.Label.valueOf(in.readUTF());
      option.asiComment = Assembler.Comment.valueOf(in.readUTF());
      option.asiBlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.asiByte = Assembler.Byte.valueOf(in.readUTF());
      option.asiWord = Assembler.Word.valueOf(in.readUTF());
      option.asiWordSwapped = Assembler.WordSwapped.valueOf(in.readUTF()); 
      option.asiTribyte = Assembler.Tribyte.valueOf(in.readUTF());
      option.asiLong = Assembler.Long.valueOf(in.readUTF());
      option.asiAddress = Assembler.Address.valueOf(in.readUTF());
      option.asiStackWord = Assembler.StackWord.valueOf(in.readUTF());
      option.asiMonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.asiMultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());      
      option.asiText = Assembler.Text.valueOf(in.readUTF());
      option.asiNumText = Assembler.NumText.valueOf(in.readUTF());
      option.asiZeroText = Assembler.ZeroText.valueOf(in.readUTF()); 
      option.asiHighText = Assembler.HighText.valueOf(in.readUTF()); 
      option.asiShiftText = Assembler.ShiftText.valueOf(in.readUTF());
      option.asiScreenText = Assembler.ScreenText.valueOf(in.readUTF()); 
      option.asiPetasciiText = Assembler.PetasciiText.valueOf(in.readUTF());     
      
      option.mergeBlocks = in.readBoolean();
      option.commentOdysseyBiosRam = in.readBoolean();
      
    } catch (FileNotFoundException e) {
         return true; 
    } catch (Exception e) {
        System.err.println(e);
        return false;
      }  
    return true;
  }
  
  /**
   * Write the option output file
   * 
   * @param file the file to write
   * @param option the option to write
   * @return true if operation is ok
   */
  public boolean writeOptionFile(File file, Option option) {
    try {      
      DataOutputStream out=new DataOutputStream(
                           new BufferedOutputStream(
                           new FileOutputStream(file))); 
      
      out.writeBoolean(option.opcodeUpperCasePreview);
      out.writeBoolean(option.opcodeUpperCaseSource);
      out.writeByte(option.illegalOpcodeMode);
      out.writeByte(option.commentLanguage);
      out.writeBoolean(option.useAsCode);
      out.writeBoolean(option.eraseDComm);
      out.writeBoolean(option.erasePlus);
      out.writeInt(option.maxLabelLength);
      out.writeInt(option.maxByteAggregate);
      
      out.writeUTF(option.psidInitSongsLabel);
      out.writeUTF(option.psidPlaySoundsLabel);
      out.writeUTF(option.sidFreqLoLabel);
      out.writeUTF(option.sidFreqHiLabel);
      
      // 0.8
      out.writeInt(option.theme);
      out.writeUTF(option.lafName);
      out.writeUTF(option.flatLaf);
      
      // 0.9
      out.writeInt(option.numInstrSpaces);
      out.writeInt(option.numInstrTabs);
      out.writeInt(option.numDataSpaces);
      out.writeInt(option.numDataTabs);
      out.writeBoolean(option.labelOnSepLine);
      out.writeBoolean(option.clickDcErase);
      out.writeBoolean(option.clickDlErase);
      out.writeBoolean(option.clickUbEdit);
      out.writeBoolean(option.clickUcEdit);
      out.writeBoolean(option.clickUlEdit);   
      out.writeBoolean(option.forceCompilation); 
      
      out.writeInt(option.maxWordAggregate);
      out.writeInt(option.maxTribyteAggregate);
      out.writeInt(option.maxLongAggregate);
      out.writeInt(option.maxSwappedAggregate);
      out.writeInt(option.maxTextAggregate);
      out.writeInt(option.maxAddressAggregate);
      out.writeInt(option.maxStackWordAggregate);
      out.writeUTF(option.tmpPath);
      
      out.writeBoolean(option.dasmF3Comp);
      out.writeUTF(option.dasmStarting.name());
      out.writeUTF(option.dasmOrigin.name());
      out.writeUTF(option.dasmLabel.name());
      out.writeUTF(option.dasmComment.name());
      out.writeUTF(option.dasmBlockComment.name());
      out.writeUTF(option.dasmByte.name());
      out.writeUTF(option.dasmWord.name());   
      out.writeUTF(option.dasmWordSwapped.name());
      out.writeUTF(option.dasmTribyte.name());
      out.writeUTF(option.dasmLong.name());
      out.writeUTF(option.dasmAddress.name());
      out.writeUTF(option.dasmStackWord.name());
      out.writeUTF(option.dasmMonoSprite.name());
      out.writeUTF(option.dasmMultiSprite.name());
      out.writeUTF(option.dasmText.name());
      out.writeUTF(option.dasmNumText.name());
      out.writeUTF(option.dasmZeroText.name());
      out.writeUTF(option.dasmHighText.name());
      out.writeUTF(option.dasmShiftText.name());
      out.writeUTF(option.dasmScreenText.name());
      out.writeUTF(option.dasmPetasciiText.name());
      
      out.writeUTF(option.tmpxStarting.name());
      out.writeUTF(option.tmpxOrigin.name());
      out.writeUTF(option.tmpxLabel.name());
      out.writeUTF(option.tmpxComment.name());
      out.writeUTF(option.tmpxBlockComment.name());
      out.writeUTF(option.tmpxByte.name());
      out.writeUTF(option.tmpxWord.name());
      out.writeUTF(option.tmpxWordSwapped.name());
      out.writeUTF(option.tmpxTribyte.name());
      out.writeUTF(option.tmpxLong.name());
      out.writeUTF(option.tmpxAddress.name());
      out.writeUTF(option.tmpxStackWord.name());
      out.writeUTF(option.tmpxMonoSprite.name());
      out.writeUTF(option.tmpxMultiSprite.name());
      out.writeUTF(option.tmpxText.name());
      out.writeUTF(option.tmpxNumText.name());
      out.writeUTF(option.tmpxZeroText.name());
      out.writeUTF(option.tmpxHighText.name());
      out.writeUTF(option.tmpxShiftText.name());
      out.writeUTF(option.tmpxScreenText.name());
      out.writeUTF(option.tmpxPetasciiText.name());
      
      out.writeUTF(option.ca65Starting.name());
      out.writeUTF(option.ca65Origin.name());
      out.writeUTF(option.ca65Label.name());
      out.writeUTF(option.ca65Comment.name());
      out.writeUTF(option.ca65BlockComment.name());
      out.writeUTF(option.ca65Byte.name());
      out.writeUTF(option.ca65Word.name()); 
      out.writeUTF(option.ca65WordSwapped.name());
      out.writeUTF(option.ca65Tribyte.name());
      out.writeUTF(option.ca65Long.name());
      out.writeUTF(option.ca65Address.name());
      out.writeUTF(option.ca65StackWord.name());
      out.writeUTF(option.ca65MonoSprite.name());
      out.writeUTF(option.ca65MultiSprite.name());
      out.writeUTF(option.ca65Text.name());
      out.writeUTF(option.ca65NumText.name());
      out.writeUTF(option.ca65ZeroText.name());
      out.writeUTF(option.ca65HighText.name());
      out.writeUTF(option.ca65ShiftText.name());
      out.writeUTF(option.ca65ScreenText.name());
      out.writeUTF(option.ca65PetasciiText.name());
      
      out.writeUTF(option.acmeStarting.name());
      out.writeUTF(option.acmeOrigin.name());
      out.writeUTF(option.acmeLabel.name());
      out.writeUTF(option.acmeComment.name());
      out.writeUTF(option.acmeBlockComment.name());
      out.writeUTF(option.acmeByte.name());
      out.writeUTF(option.acmeWord.name()); 
      out.writeUTF(option.acmeWordSwapped.name()); 
      out.writeUTF(option.acmeTribyte.name()); 
      out.writeUTF(option.acmeLong.name());
      out.writeUTF(option.acmeAddress.name());
      out.writeUTF(option.acmeStackWord.name());
      out.writeUTF(option.acmeMonoSprite.name());
      out.writeUTF(option.acmeMultiSprite.name());
      out.writeUTF(option.acmeText.name());
      out.writeUTF(option.acmeNumText.name());
      out.writeUTF(option.acmeZeroText.name());
      out.writeUTF(option.acmeHighText.name());
      out.writeUTF(option.acmeShiftText.name());
      out.writeUTF(option.acmeScreenText.name());
      out.writeUTF(option.acmePetasciiText.name());
      
      out.writeBoolean(option.kickColonMacro);
      out.writeUTF(option.kickStarting.name());
      out.writeUTF(option.kickOrigin.name());
      out.writeUTF(option.kickLabel.name());
      out.writeUTF(option.kickComment.name());
      out.writeUTF(option.kickBlockComment.name());
      out.writeUTF(option.kickByte.name());
      out.writeUTF(option.kickWord.name());
      out.writeUTF(option.kickWordSwapped.name());
      out.writeUTF(option.kickTribyte.name());
      out.writeUTF(option.kickLong.name());
      out.writeUTF(option.kickAddress.name());
      out.writeUTF(option.kickStackWord.name());
      out.writeUTF(option.kickMonoSprite.name());
      out.writeUTF(option.kickMultiSprite.name());
      out.writeUTF(option.kickText.name());
      out.writeUTF(option.kickNumText.name());
      out.writeUTF(option.kickZeroText.name());  
      out.writeUTF(option.kickHighText.name());
      out.writeUTF(option.kickShiftText.name());
      out.writeUTF(option.kickScreenText.name());
      out.writeUTF(option.kickPetasciiText.name());
      
      out.writeUTF(option.tass64Starting.name());
      out.writeUTF(option.tass64Origin.name());
      out.writeUTF(option.tass64Label.name());
      out.writeUTF(option.tass64Comment.name());
      out.writeUTF(option.tass64BlockComment.name());
      out.writeUTF(option.tass64Byte.name());
      out.writeUTF(option.tass64Word.name());
      out.writeUTF(option.tass64WordSwapped.name());
      out.writeUTF(option.tass64Tribyte.name()); 
      out.writeUTF(option.tass64Long.name());
      out.writeUTF(option.tass64Address.name());
      out.writeUTF(option.tass64StackWord.name());
      out.writeUTF(option.tass64MonoSprite.name());
      out.writeUTF(option.tass64MultiSprite.name());   
      out.writeUTF(option.tass64Text.name());
      out.writeUTF(option.tass64NumText.name());
      out.writeUTF(option.tass64ZeroText.name()); 
      out.writeUTF(option.tass64HighText.name());
      out.writeUTF(option.tass64ShiftText.name());
      out.writeUTF(option.tass64ScreenText.name());
      out.writeUTF(option.tass64PetasciiText.name());
      
      out.writeBoolean(option.commentC64ZeroPage);
      out.writeBoolean(option.commentC64StackArea);
      out.writeBoolean(option.commentC64_200Area);
      out.writeBoolean(option.commentC64_300Area);
      out.writeBoolean(option.commentC64ScreenArea);
      out.writeBoolean(option.commentC64BasicFreeArea);
      out.writeBoolean(option.commentC64BasicRom);
      out.writeBoolean(option.commentC64FreeRam);
      out.writeBoolean(option.commentC64VicII);
      out.writeBoolean(option.commentC64Sid);
      out.writeBoolean(option.commentC64ColorArea);          
      out.writeBoolean(option.commentC64Cia1);
      out.writeBoolean(option.commentC64Cia2);
      out.writeBoolean(option.commentC64KernalRom);
      
      out.writeBoolean(option.commentC128ZeroPage);
      out.writeBoolean(option.commentC128StackArea);
      out.writeBoolean(option.commentC128_200Area);
      out.writeBoolean(option.commentC128_300Area);
      out.writeBoolean(option.commentC128ScreenArea);
      out.writeBoolean(option.commentC128AppProgArea);  
      out.writeBoolean(option.commentC128BasicRom);  
      out.writeBoolean(option.commentC128Cia1);
      out.writeBoolean(option.commentC128Cia2);
      out.writeBoolean(option.commentC128Color);
      out.writeBoolean(option.commentC128DMA);  
      out.writeBoolean(option.commentC128KernalRom);
      out.writeBoolean(option.commentC128MMU);
      out.writeBoolean(option.commentC128ScreenMem);
      out.writeBoolean(option.commentC128UserBasic);
      out.writeBoolean(option.commentC128VDC);
      out.writeBoolean(option.commentC128VideoColor);
      out.writeBoolean(option.commentC128VicII);
      out.writeBoolean(option.commentC128Sid);
      
      out.writeBoolean(option.commentC1541ZeroPage);
      out.writeBoolean(option.commentC1541StackArea);
      out.writeBoolean(option.commentC1541_200Area);
      out.writeBoolean(option.commentC1541Buffer0);
      out.writeBoolean(option.commentC1541Buffer1);
      out.writeBoolean(option.commentC1541Buffer2);
      out.writeBoolean(option.commentC1541Buffer3);
      out.writeBoolean(option.commentC1541Buffer4);
      out.writeBoolean(option.commentC1541Via1);
      out.writeBoolean(option.commentC1541Via2);
      out.writeBoolean(option.commentC1541Kernal);
      
      out.writeBoolean(option.commentPlus4ZeroPage);
      out.writeBoolean(option.commentPlus4StackArea);
      out.writeBoolean(option.commentPlus4_200Area);
      out.writeBoolean(option.commentPlus4_300Area);
      out.writeBoolean(option.commentPlus4_400Area);
      out.writeBoolean(option.commentPlus4_500Area);
      out.writeBoolean(option.commentPlus4_600Area);
      out.writeBoolean(option.commentPlus4_700Area);
      out.writeBoolean(option.commentPlus4ColorArea);
      out.writeBoolean(option.commentPlus4VideoArea);
      out.writeBoolean(option.commentPlus4BasicRamP);
      out.writeBoolean(option.commentPlus4BasicRamN);
      out.writeBoolean(option.commentPlus4Luminance);
      out.writeBoolean(option.commentPlus4ColorBitmap);
      out.writeBoolean(option.commentPlus4GraphicData);
      out.writeBoolean(option.commentPlus4BasicRom);
      out.writeBoolean(option.commentPlus4BasicExt);
      out.writeBoolean(option.commentPlus4Caracter);
      out.writeBoolean(option.commentPlus4Acia);
      out.writeBoolean(option.commentPlus4_6529B_1); 
      out.writeBoolean(option.commentPlus4_6529B_2);
      out.writeBoolean(option.commentPlus4Ted);
      out.writeBoolean(option.commentPlus4Kernal);
      
      out.writeBoolean(option.commentVic20ZeroPage);
      out.writeBoolean(option.commentVic20StackArea);
      out.writeBoolean(option.commentVic20_200Area);
      out.writeBoolean(option.commentVic20_300Area);
      out.writeBoolean(option.commentVic20_400Area);
      out.writeBoolean(option.commentVic20Vic);
      out.writeBoolean(option.commentVic20Via1);
      out.writeBoolean(option.commentVic20Via2);
      out.writeBoolean(option.commentVic20UserBasic);
      out.writeBoolean(option.commentVic20Screen);
      out.writeBoolean(option.commentVic20_8kExp1);
      out.writeBoolean(option.commentVic20_8kExp2);
      out.writeBoolean(option.commentVic20_8kExp3);
      out.writeBoolean(option.commentVic20Character);
      out.writeBoolean(option.commentVic20Color);
      out.writeBoolean(option.commentVic20Block2);
      out.writeBoolean(option.commentVic20Block3);
      out.writeBoolean(option.commentVic20Block4);
      out.writeBoolean(option.commentVic20BasicRom);
      out.writeBoolean(option.commentVic20KernalRom); 

      out.writeUTF(option.assembler.name());
      
      // 1.0
      out.writeByte(option.memoryValue);
      out.writeInt(option.numInstrCSpaces);
      out.writeInt(option.numInstrCTabs);
      out.writeInt(option.numDataCSpaces);
      out.writeInt(option.numDataCTabs);  
      
      // 1.2
      out.writeBoolean(option.allowUtf);
      
      // 1.6
      out.writeBoolean(option.useSidFreq);
      out.writeBoolean(option.sidFreqMarkMem);
      out.writeBoolean(option.sidFreqCreateLabel);
      out.writeBoolean(option.sidFreqCreateComment);
      out.writeBoolean(option.createPSID);
      out.writeBoolean(option.notMarkPSID);
      out.writeInt(option.heather);
      out.writeUTF(option.custom);
      out.writeBoolean(option.repositionate);
      
      // 1.7
      out.writeBoolean(option.pedantic);
      out.writeBoolean(option.clickVlPatch);
      
      // 2.1
      out.writeUTF(option.glassStarting.name());
      out.writeUTF(option.glassOrigin.name());
      out.writeUTF(option.glassLabel.name());
      out.writeUTF(option.glassComment.name());
      out.writeUTF(option.glassBlockComment.name());
      out.writeUTF(option.glassByte.name());
      out.writeUTF(option.glassWord.name());
      out.writeUTF(option.glassWordSwapped.name());
      out.writeUTF(option.glassTribyte.name()); 
      out.writeUTF(option.glassLong.name());
      out.writeUTF(option.glassAddress.name());
      out.writeUTF(option.glassStackWord.name());
      out.writeUTF(option.glassMonoSprite.name());
      out.writeUTF(option.glassMultiSprite.name());   
      out.writeUTF(option.glassText.name());
      out.writeUTF(option.glassNumText.name());
      out.writeUTF(option.glassZeroText.name()); 
      out.writeUTF(option.glassHighText.name());
      out.writeUTF(option.glassShiftText.name());
      out.writeUTF(option.glassScreenText.name());
      out.writeUTF(option.glassPetasciiText.name());
      
      //2.3
      out.writeInt(option.numSpacesOp);
      out.writeInt(option.numTabsOp);
      out.writeInt(option.sizePreviewFont);
      out.writeInt(option.sizeSourceFont);
      
      // 2.4
      out.writeBoolean(option.createSAP);
      out.writeBoolean(option.notMarkSAP);
      out.writeBoolean(option.commentAtariZeroPage);
      out.writeBoolean(option.commentAtariStackArea);
      out.writeBoolean(option.commentAtari_200Area);
      out.writeBoolean(option.commentAtari_300Area);
      out.writeBoolean(option.commentAtari_400Area);
      out.writeBoolean(option.commentAtari_500Area);
      out.writeBoolean(option.commentAtariCartridgeB);
      out.writeBoolean(option.commentAtariCartridgeA);
      out.writeBoolean(option.commentAtariGtia);
      out.writeBoolean(option.commentAtariPokey);
      out.writeBoolean(option.commentAtariPia);
      out.writeBoolean(option.commentAtariAntic);
      out.writeBoolean(option.commentAtariKernalRom);
      
      // 2.5
      out.writeInt(option.dotsType);
      out.writeBoolean(option.noUndocumented);
      
      // 2.6
      out.writeBoolean(option.chooserPerc);
      out.writeBoolean(option.showMiniature);
      
      // 2.7
      out.writeUTF(option.syntaxTheme);
      
      // 2.8
      out.writeBoolean(option.showSidId);
      out.writeUTF(option.sidIdPath);
      
      // 2.9
      out.writeBoolean(option.sidFreqLinearTable);
      out.writeBoolean(option.sidFreqCombinedTable);
      out.writeBoolean(option.sidFreqInverseLinearTable);
      out.writeBoolean(option.sidFreqLinearOctNoteTable);
      out.writeBoolean(option.sidFreqHiOct13Table);
      out.writeBoolean(option.sidFreqLinearScaleTable);
      out.writeBoolean(option.sidFreqShortLinearTable);
      out.writeBoolean(option.sidFreqShortCombinedTable);
      out.writeBoolean(option.sidFreqHiOctCombinedTable);
      out.writeBoolean(option.sidFreqHiOctCombinedInvertedTable);
      out.writeBoolean(option.sidFreqLoOctCombinedTable);
      out.writeBoolean(option.sidFreqHiOct12Table); 

      // 3.0
      out.writeUTF(option.asmStarting.name());
      out.writeUTF(option.asmOrigin.name());
      out.writeUTF(option.asmLabel.name());
      out.writeUTF(option.asmComment.name());
      out.writeUTF(option.asmBlockComment.name());
      out.writeUTF(option.asmByte.name());
      out.writeUTF(option.asmWord.name());
      out.writeUTF(option.asmWordSwapped.name());
      out.writeUTF(option.asmTribyte.name()); 
      out.writeUTF(option.asmLong.name());
      out.writeUTF(option.asmAddress.name());
      out.writeUTF(option.asmStackWord.name());
      out.writeUTF(option.asmMonoSprite.name());
      out.writeUTF(option.asmMultiSprite.name());   
      out.writeUTF(option.asmText.name());
      out.writeUTF(option.asmNumText.name());
      out.writeUTF(option.asmZeroText.name()); 
      out.writeUTF(option.asmHighText.name());
      out.writeUTF(option.asmShiftText.name());
      out.writeUTF(option.asmScreenText.name());
      out.writeUTF(option.asmPetasciiText.name());
      
      out.writeUTF(option.asiStarting.name());
      out.writeUTF(option.asiOrigin.name());
      out.writeUTF(option.asiLabel.name());
      out.writeUTF(option.asiComment.name());
      out.writeUTF(option.asiBlockComment.name());
      out.writeUTF(option.asiByte.name());
      out.writeUTF(option.asiWord.name());
      out.writeUTF(option.asiWordSwapped.name());
      out.writeUTF(option.asiTribyte.name()); 
      out.writeUTF(option.asiLong.name());
      out.writeUTF(option.asiAddress.name());
      out.writeUTF(option.asiStackWord.name());
      out.writeUTF(option.asiMonoSprite.name());
      out.writeUTF(option.asiMultiSprite.name());   
      out.writeUTF(option.asiText.name());
      out.writeUTF(option.asiNumText.name());
      out.writeUTF(option.asiZeroText.name()); 
      out.writeUTF(option.asiHighText.name());
      out.writeUTF(option.asiShiftText.name());
      out.writeUTF(option.asiScreenText.name());
      out.writeUTF(option.asiPetasciiText.name());
            
      out.writeBoolean(option.mergeBlocks);
      out.writeBoolean(option.commentOdysseyBiosRam);

      out.flush();
      out.close();
    } catch (Exception e) {
        System.err.println(e);
        return false;
      }  
    return true;
  }    
  
  /**
   * Read the project from file 
   * 
   * @param file the file to read
   * @param project the project to fill from file
   * @return true if operation is ok
   */
  public boolean readProjectFile(File file, Project project) {
    try {      
      MemoryDasm mem;  
      Relocate relocate;
      Patch patch;
      Freeze freeze;
      DataInputStream in;
    
      if (isGZipped(file)) in=new DataInputStream(
              new GZIPInputStream(
              new BufferedInputStream(
              new FileInputStream(file)))); 
          
      else in=new DataInputStream(
              new BufferedInputStream(
              new FileInputStream(file)));  
      
      byte version=in.readByte();

      project.name=in.readUTF();
      project.file=in.readUTF();
      project.description=in.readUTF();
      project.fileType=FileType.valueOf(in.readUTF());
      if (version>0) project.targetType=TargetType.valueOf(in.readUTF());  
      else project.targetType=TargetType.C64;  
      
      int size=in.readInt();      
      project.inB=new byte[size];
      
      ///in.read(project.inB);      
      for (int i=0; i<size;i++) {
        project.inB[i]=in.readByte();
      }
      
      size=in.readInt();
      project.memoryFlags=new byte[size];
      
      ///in.read(project.memoryFlags);
      for (int i=0; i<size;i++) {
        project.memoryFlags[i]=in.readByte();
      }           
      
      size=in.readInt();
      project.memory=new MemoryDasm[size];
      for (int i=0; i<project.memory.length; i++) {
        mem=new MemoryDasm();        
        
        mem.address=in.readInt();
        
        if (in.readBoolean()) mem.dasmComment=in.readUTF();
        else mem.dasmComment=null;
        
        if (in.readBoolean()) mem.userComment=in.readUTF();
        else mem.userComment=null;
        
        if (in.readBoolean()) mem.userBlockComment=in.readUTF();
        else mem.userBlockComment=null;
        
        if (in.readBoolean()) mem.dasmLocation=in.readUTF();
        else mem.dasmLocation=null;
        
        if (in.readBoolean()) mem.userLocation=in.readUTF();
        else mem.userLocation=null;
        
        mem.isInside=in.readBoolean();
        mem.isCode=in.readBoolean();
        mem.isData=in.readBoolean();
        
        if (version>0) {
            mem.isGarbage=in.readBoolean();
            mem.dataType=DataType.valueOf(in.readUTF());
        } // version 1
        
        mem.copy=in.readByte();
        mem.related=in.readInt();
        mem.type=in.readChar();
        
        if (project.fileType==FileType.MPR) {
          project.mpr=new MPR();
          project.mpr.getElements(project.inB);
        }
        
        if (version>2) { // version 3
          mem.index=in.readByte();
        }
        
        if (version>8) {  // version 9
          mem.relatedAddressBase=in.readInt();
          mem.relatedAddressDest=in.readInt();
        }
        
        if (version>9) {  // version 10
          mem.basicType=BasicType.valueOf(in.readUTF());
        }        
        
        project.memory[i]=mem;  
      }
      
      if (version>1) project.chip=in.readInt(); // version 2
      
      if (version>2)  {                         // version 3
        for (int i=0; i<Constant.MIN_COLS; i++) {
          for (int j=0; j<Constant.MIN_ROWS; j++) {
            if (in.readBoolean()) project.constant.table[i][j]=in.readUTF();
            else project.constant.table[i][j]=null;   
          }  
        }
      }
      
      if (version>3)  {                         // version 4
        size=in.readInt();
        if (size==0) project.relocates=null;
        else {
          project.relocates=new Relocate[size];
          for (int i=0; i<size; i++) {              
            relocate=new Relocate();
            relocate.fromStart=in.readInt();
            relocate.fromEnd=in.readInt();
            relocate.toStart=in.readInt();
            relocate.toEnd=in.readInt();
            project.relocates[i]=relocate;
          }
        }
      }
      
      if (version>4)  {                         // version 5
        size=in.readInt();
        if (size==0) project.patches=null;
        else {
          project.patches=new Patch[size];
          for (int i=0; i<size; i++) {              
            patch=new Patch();
            patch.address=in.readInt();
            patch.value=in.readInt();
            project.patches[i]=patch;
          }
        }
      }  
      
      if (version>5)  {                         // version 6
        for (int i=0; i<Constant.MIN_COLS; i++) {
          for (int j=Constant.MIN_ROWS; j<Constant.ROWS; j++) {
            if (in.readBoolean()) project.constant.table[i][j]=in.readUTF();
            else project.constant.table[i][j]=null;   
          }  
        }
      }
      
      if (version>6)  {                         // version 7
        size=in.readInt();
        if (size==0) project.freezes=null;
        else {
          project.freezes=new Freeze[size];
          for (int i=0; i<size; i++) {              
            freeze=new Freeze();
            freeze.name=in.readUTF();
            if (in.readBoolean()) {
              int size2=in.readInt();
              byte[] buf=new byte[size2];
              for (int j=0; j<size2; j++) {
                buf[j]=in.readByte();
              } 
              freeze.text=new String(buf, "utf-8");                            
            } else {
                freeze.text=in.readUTF();
              }
            project.freezes[i]=freeze;
          }
        }
      }
      
      if (version>7)  {                         // version 8
        for (int i=Constant.MIN_COLS; i<Constant.COLS; i++) {
          for (int j=0; j<Constant.ROWS; j++) {
            if (in.readBoolean()) project.constant.table[i][j]=in.readUTF();
            else project.constant.table[i][j]=null;   
          }  
        }
      }
      
      if (version>9)  {                         // version 10
        for (int i=0; i<Constant.COLS; i++) {
          for (int j=0; j<Constant.ROWS; j++) {
            if (in.readBoolean()) project.constant.comment[i][j]=in.readUTF();
            else project.constant.comment[i][j]=null;   
          }  
        }
      }    
      
      if (version>10) project.binAddress=in.readInt(); // version 11
    } catch (Exception e) {
        System.err.println(e);
        return false;
      }  
      
    return true;  
  }
  
  /**
   * Write the project output file
   * 
   * @param file the file to write
   * @param project the project to write
   * @return true if operation is ok
   */
  public boolean writeProjectFile(File file, Project project) {
    try {      
      DataOutputStream out=new DataOutputStream(
                           new GZIPOutputStream(
                           new BufferedOutputStream(
                           new FileOutputStream(file)))); 
      
      out.writeByte(project.ACTUAL_VERSION);
      
      out.writeUTF(project.name);
      out.writeUTF(project.file);
      out.writeUTF(project.description);
      out.writeUTF(project.fileType.name());
      out.writeUTF(project.targetType.name());  // version 1
      
      out.writeInt(project.inB.length);
      out.write(project.inB);
      
      out.writeInt(project.memoryFlags.length);
      out.write(project.memoryFlags);
      
      out.writeInt(project.memory.length);
      for (MemoryDasm memory : project.memory) {
          out.writeInt(memory.address);
          
          if (memory.dasmComment!=null) {
              out.writeBoolean(true);
              out.writeUTF(memory.dasmComment);
          } else {
              out.writeBoolean(false);
            }
          
          if (memory.userComment!=null) {
              out.writeBoolean(true);
              out.writeUTF(memory.userComment);
          } else {
              out.writeBoolean(false);
            }
          
          if (memory.userBlockComment!=null) {
              out.writeBoolean(true);
              out.writeUTF(memory.userBlockComment);
          } else {
              out.writeBoolean(false);
            }         
          
          if (memory.dasmLocation!=null) {
              out.writeBoolean(true);
              out.writeUTF(memory.dasmLocation);
          } else {
              out.writeBoolean(false);
            }
          
          if (memory.userLocation!=null) {
              out.writeBoolean(true);
              out.writeUTF(memory.userLocation);
          } else {
              out.writeBoolean(false);
            }      
          
          out.writeBoolean(memory.isInside);
          out.writeBoolean(memory.isCode);
          out.writeBoolean(memory.isData);
          out.writeBoolean(memory.isGarbage);    // version 1
          out.writeUTF(memory.dataType.name());  // version 1
          out.writeByte(memory.copy);
          out.writeInt(memory.related);
          out.writeChar(memory.type);
          
          out.writeByte(memory.index);   // version 3
          
          out.writeInt(memory.relatedAddressBase); // version 9
          out.writeInt(memory.relatedAddressDest); // version 9
          out.writeUTF(memory.basicType.name());   // version 10
      }  
      
      out.writeInt(project.chip);  // version 2
      
      // version 3
      for (int i=0; i<Constant.MIN_COLS; i++) {
        for (int j=0; j<Constant.MIN_ROWS; j++) {
          if (project.constant.table[i][j]!=null) {
            out.writeBoolean(true);
            out.writeUTF(project.constant.table[i][j]);  
          } else {
              out.writeBoolean(false);
            } 
        }  
      }
      
      // version 4
      if (project.relocates==null) out.writeInt(0);
      else {
        out.writeInt(project.relocates.length);
        for (Relocate relocate:project.relocates) {
          out.writeInt(relocate.fromStart);
          out.writeInt(relocate.fromEnd);
          out.writeInt(relocate.toStart);
          out.writeInt(relocate.toEnd);
        }
      }
      
      // version 5
      if (project.patches==null) out.writeInt(0);
      else {
        out.writeInt(project.patches.length);
        for (Patch patch:project.patches) {
          out.writeInt(patch.address);
          out.writeInt(patch.value);
        }
      }    
      
      // version 6
      for (int i=0; i<Constant.MIN_COLS; i++) {
        for (int j=Constant.MIN_ROWS; j<Constant.ROWS; j++) {
          if (project.constant.table[i][j]!=null) {
            out.writeBoolean(true);
            out.writeUTF(project.constant.table[i][j]);  
          } else {
              out.writeBoolean(false);
            } 
        }  
      }  
      
      // version 7
      if (project.freezes==null) out.writeInt(0);
      else {
        out.writeInt(project.freezes.length);
        for (Freeze freeze:project.freezes) {
          out.writeUTF(freeze.name);
          
          // writeUTF is limited to FFFF size, so apply different way
          if (freeze.text.length()>0xFFFF) {
            out.writeBoolean(true);  
            byte[] buf=freeze.text.getBytes("utf-8");
            out.writeInt(buf.length);
            out.write(buf);           
          } else {
              out.writeBoolean(false);
              out.writeUTF(freeze.text);
            }
        }
      }  
      
      // version 8
      for (int i=Constant.MIN_COLS; i<Constant.COLS; i++) {
        for (int j=0; j<Constant.ROWS; j++) {
          if (project.constant.table[i][j]!=null) {
            out.writeBoolean(true);
            out.writeUTF(project.constant.table[i][j]);  
          } else {
              out.writeBoolean(false);
            } 
        }          
      }
      
      // version 10
      for (int i=0; i<Constant.COLS; i++) {
        for (int j=0; j<Constant.ROWS; j++) {
          if (project.constant.comment[i][j]!=null) {
            out.writeBoolean(true);
            out.writeUTF(project.constant.comment[i][j]);  
          } else {
              out.writeBoolean(false);
            } 
        }          
      }      
      
      // version 11
      out.writeInt(project.binAddress);  
      
      out.flush();
      out.close();
    } catch (Exception e) {
        System.err.println(e);
        return false;
      }  
    return true;
  }  
  
  /**
   * Read percent of the project from file 
   * This is approssimate as not all rules as follow to speed up the read and
   * else the MPR is not coved
   * 
   * @param file the file to read
   * @return >0 as percent, -1 as error
   */
  public int readPercProjectFile(File file) {
    int total=0;
    int done=0;
    
    try {      
      MemoryDasm mem;  
      DataInputStream in;
 
      if (isGZipped(file)) in=new DataInputStream(
              new GZIPInputStream(
              new BufferedInputStream(
              new FileInputStream(file)))); 
          
      else in=new DataInputStream(
              new BufferedInputStream(
              new FileInputStream(file)));  
      
      byte version=in.readByte();

      in.skipNBytes((int)in.readChar());    //name
      in.skipNBytes((int)in.readChar());   // file 
      in.skipNBytes((int)in.readChar());   // description 
      in.skipNBytes((int)in.readChar());   // file type
      if (version>0) in.skipNBytes((int)in.readChar()); //target 
      
      int size=in.readInt();   
      in.skipNBytes(size);      // inB
      
      size=in.readInt();
      in.skipNBytes(size);      // memoryFlags
      
      boolean isInside;
      boolean isGarbage;
      boolean dasmLoc;
      boolean userLoc;

      size=in.readInt();
      for (int i=0; i<size; i++) {        
        in.readInt(); // address
        
        if (in.readBoolean()) in.skipNBytes((int)in.readChar()); //dasmComment       
        if (in.readBoolean()) in.skipNBytes((int)in.readChar());  // userComemnt        
        if (in.readBoolean()) in.skipNBytes((int)in.readChar()); // userBlockComment
        
        if (in.readBoolean()) {
          in.skipNBytes((int)in.readChar());  // dasmLocation
          dasmLoc=true;
        } else dasmLoc=false;
        
        
        if (in.readBoolean()) {
          in.skipNBytes((int)in.readChar());   // userLocation
          userLoc=true;
        } else userLoc=false;
        
        isInside=in.readBoolean(); // isInside
        in.readBoolean();          // isCode
        in.readBoolean();          // isData
        
        if (version>0) {
            isGarbage=in.readBoolean();         // isGarbage
            in.skipNBytes((int)in.readChar());  // datatype
        } // version 1
        else isGarbage=false;
        
        in.readByte();  // copy
        in.readInt();   // related
        in.readChar();  // type
        
        /**
        if (project.fileType==FileType.MPR) {
          project.mpr=new MPR();
          project.mpr.getElements(project.inB);
        }
        */
        
        if (version>2) {  // version 3
          in.readByte();  // index
        }
        
        if (version>8) {  // version 9
          in.readInt();   // related base
          in.readInt();   // related dest
        }
        
        if (version>9) {  // version 10
          in.readUTF();   // basic type
        }            
        
        if (!isInside ||isGarbage) continue;
        if (userLoc) {
          total++;
          done++;
        } else {
            if (dasmLoc) total++;
        }
      }
      
      return (int)(done*100/total);  // avoid that total should be zero
    } catch (Exception e) {
        System.err.println(e);
        return -1;
      }        
  }
  
  /**
   * Read the file to disassemble
   * 
   * @param inN the input name
   * @return the buffer with data
   * @throws java.io.FileNotFoundException
   * @throws IOException
   * @throws SecurityException
   */
  public byte[] readFile(String inN) throws FileNotFoundException, IOException, SecurityException {       
    int size;
    BufferedInputStream inF;  
    File file=new File(inN);
    byte[] inB = new byte[(int)file.length()];   
    
    // see if the file is present
    inF=new BufferedInputStream(new FileInputStream(file));
                 
    // read the file
     size=inF.read(inB);
     inF.close();
     
     if (size<=0) return null;
     
     // resize to the read size of read byte
     byte[] res=new byte[size];
     System.arraycopy(inB, 0, res, 0, size);
     return res;
  }
  
  /**
   * Write the buffer to file
   * 
   * @param file the file to create
   * @param inB the buffer to write
   * @return true if operation is ok
   */
  public boolean writeFile(File file, byte[]inB) {
    try {
      BufferedOutputStream outF=new BufferedOutputStream(new FileOutputStream(file));
      outF.write(inB, 0, inB.length);
      outF.flush();
      outF.close();        
    } catch (Exception e) {
       return false; 
      }        
    return true;
  }

  /**
   * Wtyie the given text to file
   * 
   * @param file the file to use
   * @param text the text to write
   * @return true if operation is ok
   */
    public boolean writeTxtFile(File file, String text) {
      try (
        FileWriter writer=new FileWriter(file);
        BufferedWriter bw=new BufferedWriter(writer)) {
        bw.write(text);
      } catch (IOException e) {
          System.err.println(e);
          return false;
        }
      return true;  
    }
       
    /**
     * Write the constant column to file
     * 
     * @param file the file to read
     * @param constant the constant container
     * @param col the column to use
     * @return true if operation is ok 
     */
    public boolean writeConstantFile(File file, Constant constant, int col) {
      try {
        DataOutputStream out=new DataOutputStream(
                           new BufferedOutputStream(
                           new FileOutputStream(file)));
          
        out.writeUTF(HEADER_CST);              // write header    
        out.writeInt(1);                       // write version   
        
        String[] values=constant.table[col];
        out.writeInt(values.length);
        for (String val:values) {
          if (val==null) out.writeBoolean(false);
          else {
            out.writeBoolean(true);
            out.writeUTF(val);
          }
        }
        
        out.close();
        out.flush();          
      } catch (Exception e) {
          System.err.println(e);
          return false; 
      }
      return true;
    }
    
    /**
     * Read the constant column to file
     * 
     * @param file the file to read
     * @param constant the constant container
     * @param col the column to use
     * @return true if operation is ok 
     */
    public boolean readConstantFile(File file, Constant constant, int col) {
      try {
        DataInputStream in=new DataInputStream(
                           new BufferedInputStream(
                           new FileInputStream(file)));
        
        String header=in.readUTF();
    
        // test header
        if (!header.equals((HEADER_CST))) {
          return false;
        }
    
        // test for valid version
        int version=in.readInt();
        if (version !=1) {
          return false;
        }     

        int size=in.readInt();
        
        for (int i=0; i<size; i++) {
          if (in.readBoolean()) constant.table[col][i]=in.readUTF();
          else constant.table[col][i]=null;
        }
        
        in.close();         
      } catch (Exception e) {
          System.err.println(e);
          return false; 
      }
      return true;
    }
    
  /**
    * Checks if a file is gzipped.
    * 
    * @param file input file
    * @return true if it is gzipped
    */
   public static boolean isGZipped(File file) {
     int magic = 0;
     try {
       RandomAccessFile raf = new RandomAccessFile(file, "r");
       magic = raf.read() & 0xff | ((raf.read() << 8) & 0xff00);
       raf.close();
     } catch (Throwable e) {
         System.err.println(e);
      }
     return magic == GZIPInputStream.GZIP_MAGIC;
   }    
}
