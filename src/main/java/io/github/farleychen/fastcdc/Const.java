package io.github.farleychen.fastcdc;

/**
 * @author FengChen
 */
final class Const {

    /** Smallest acceptable value for the minimum chunk size. */
    static final int MINIMUM_MIN = 64;
    /** Largest acceptable value for the minimum chunk size. */
    static final int MINIMUM_MAX = 67_108_864;
    /** Smallest acceptable value for the average chunk size. */
    static final int AVERAGE_MIN = 256;
    /** Largest acceptable value for the average chunk size. */
    static final int AVERAGE_MAX = 268_435_456;
    /** Smallest acceptable value for the maximum chunk size. */
    static final int MAXIMUM_MIN = 1024;
    /** Largest acceptable value for the maximum chunk size. */
    static final int MAXIMUM_MAX = 1_073_741_824;

    static final int[] GEAR = {
        0x5C95_C078, 0x2240_8989, 0x2D48_A214, 0x1284_2087, 0x530F_8AFB, 0x4745_36B9, 0x2963_B4F1, 0x44CB_738B,
        0x4EA7_403D, 0x4D60_6B6E, 0x074E_C5D3, 0x3AF3_9D18, 0x7260_03CA, 0x37A6_2A74, 0x51A2_F58E, 0x7506_358E,
        0x5D4A_B128, 0x4D4A_E17B, 0x41E8_5924, 0x470C_36F7, 0x4741_CBE1, 0x01BB_7F30, 0x617C_1DE3, 0x2B0C_3A1F,
        0x50C4_8F73, 0x21A8_2D37, 0x6095_ACE0, 0x4191_67A0, 0x3CAF_49B0, 0x40CE_A62D, 0x66BC_1C66, 0x545E_1DAD,
        0x2BFA_77CD, 0x6E85_DA24, 0x5FB0_BDC5, 0x652C_FC29, 0x3A0A_E1AB, 0x2837_E0F3, 0x6387_B70E, 0x1317_6012,
        0x4362_C2BB, 0x66D8_F4B1, 0x37FC_E834, 0x2C9C_D386, 0x2114_4296, 0x6272_68A8, 0x650D_F537, 0x2805_D579,
        0x3B21_EBBD, 0x7357_ED34, 0x3F58_B583, 0x7150_DDCA, 0x7362_225E, 0x620A_6070, 0x2C5E_F529, 0x7B52_2466,
        0x768B_78C0, 0x4B54_E51E, 0x75FA_07E5, 0x06A3_5FC6, 0x30B7_1024, 0x1C86_26E1, 0x296A_D578, 0x28D7_BE2E,
        0x1490_A05A, 0x7CEE_43BD, 0x698B_56E3, 0x09DC_0126, 0x4ED6_DF6E, 0x02C1_BFC7, 0x2A59_AD53, 0x29C0_E434,
        0x7D6C_5278, 0x5079_40A7, 0x5EF6_BA93, 0x68B6_AF1E, 0x4653_7276, 0x611B_C766, 0x155C_587D, 0x301B_A847,
        0x2CC9_DDA7, 0x0A43_8E2C, 0x0A69_D514, 0x744C_72D3, 0x4F32_6B9B, 0x7EF3_4286, 0x4A0E_F8A7, 0x6AE0_6EBE,
        0x669C_5372, 0x1240_2DCB, 0x5FEA_E99D, 0x76C7_F4A7, 0x6ABD_B79C, 0x0DFA_A038, 0x20E2_282C, 0x730E_D48B,
        0x069D_AC2F, 0x168E_CF3E, 0x2610_E61F, 0x2C51_2C8E, 0x15FB_8C06, 0x5E62_BC76, 0x6955_5135, 0x0ADB_864C,
        0x4268_F914, 0x349A_B3AA, 0x20ED_FDB2, 0x5172_7981, 0x37B4_B3D8, 0x5DD1_7522, 0x6B2C_BFE4, 0x5C47_CF9F,
        0x30FA_1CCD, 0x23DE_DB56, 0x13D1_F50A, 0x64ED_DEE7, 0x0820_B0F7, 0x46E0_7308, 0x1E2D_1DFD, 0x17B0_6C32,
        0x2500_36D8, 0x284D_BF34, 0x6829_2EE0, 0x362E_C87C, 0x087C_B1EB, 0x76B4_6720, 0x1041_30DB, 0x7196_6387,
        0x482D_C43F, 0x2388_EF25, 0x5241_44E1, 0x44BD_834E, 0x448E_7DA3, 0x3FA6_EAF9, 0x3CDA_215C, 0x3A50_0CF3,
        0x395C_B432, 0x5195_129F, 0x4394_5F87, 0x5186_2CA4, 0x56EA_8FF1, 0x2010_34DC, 0x4D32_8FF5, 0x7D73_A909,
        0x6234_D379, 0x64CF_BF9C, 0x36F6_589A, 0x0A2C_E98A, 0x5FE4_D971, 0x03BC_15C5, 0x4402_1D33, 0x16C1_932B,
        0x3750_3614, 0x1ACA_F69D, 0x3F03_B779, 0x49E6_1A03, 0x1F52_D7EA, 0x1C6D_DD5C, 0x0622_18CE, 0x07E7_A11A,
        0x1905_757A, 0x7CE0_0A53, 0x49F4_4F29, 0x4BCC_70B5, 0x39FE_EA55, 0x5242_CEE8, 0x3CE5_6B85, 0x00B8_1672,
        0x46BE_ECCC, 0x3CA0_AD56, 0x2396_CEE8, 0x7854_7F40, 0x6B08_089B, 0x66A5_6751, 0x781E_7E46, 0x1E2C_F856,
        0x3BC1_3591, 0x494A_4202, 0x5204_94D7, 0x2D87_459A, 0x7575_55B6, 0x4228_4CC1, 0x1F47_8507, 0x75C9_5DFF,
        0x35FF_8DD7, 0x4E47_57ED, 0x2E11_F88C, 0x5E1B_5048, 0x420E_6699, 0x226B_0695, 0x4D16_79B4, 0x5A22_646F,
        0x161D_1131, 0x125C_68D9, 0x1313_E32E, 0x4AA8_5724, 0x21DC_7EC1, 0x4FFA_29FE, 0x7296_8382, 0x1CA8_EEF3,
        0x3F3B_1C28, 0x39C2_FB6C, 0x6D76_493F, 0x7A22_A62E, 0x789B_1C2A, 0x16E0_CB53, 0x7DEC_EEEB, 0x0DC7_E1C6,
        0x5C75_BF3D, 0x5221_8333, 0x106D_E4D6, 0x7DC6_4422, 0x6559_0FF4, 0x2C02_EC30, 0x64A9_AC67, 0x59CA_B2E9,
        0x4A21_D2F3, 0x0F61_6E57, 0x23B5_4EE8, 0x0273_0AAA, 0x2F3C_634D, 0x7117_FC6C, 0x01AC_6F05, 0x5A9E_D20C,
        0x158C_4E2A, 0x42B6_99F0, 0x0C7C_14B3, 0x02BD_9641, 0x15AD_56FC, 0x1C72_2F60, 0x7DA1_AF91, 0x23E0_DBCB,
        0x0E93_E12B, 0x64B2_791D, 0x440D_2476, 0x588E_A8DD, 0x4665_A658, 0x7446_C418, 0x1877_A774, 0x5626_407E,
        0x7F63_BD46, 0x32D2_DBD8, 0x3C79_0F4A, 0x772B_7239, 0x6F8B_2826, 0x677F_F609, 0x0DC8_2C11, 0x23FF_E354,
        0x2EAC_53A6, 0x1613_9E09, 0x0AFD_0DBC, 0x2A4D_4237, 0x56A3_68C7, 0x2343_25E4, 0x2DCE_9187, 0x32E8_EA7E,};

}
