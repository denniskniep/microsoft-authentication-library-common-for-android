﻿using System;

namespace PerfClTool.Measurement
{
    internal class PerfDataRecord
    {
        public static readonly string ValueNotApplicable = "NA";
        public PerfDataRecord(string timeStamp, string marker, string time, string thread, string cpuUsed, string cpuTotal, string residentSize, string virtualSize, string wifiSent, string wifiRecv, string wwanSent, string wwanRecv, string appSent, string appRecv, string battery, string systemDiskRead, string systemDiskWrite)
        {
            TimeStamp = timeStamp;
            Marker = marker;
            Time = time;
            Thread = thread;
            CpuUsed = cpuUsed;
            CpuTotal = cpuTotal;
            ResidentSize = residentSize;
            VirtualSize = virtualSize;
            WifiSent = wifiSent;
            WifiRecv = wifiRecv;
            WwanSent = wwanSent;
            WwanRecv = wwanRecv;
            AppSent = appSent;
            AppRecv = appRecv;
            Battery = battery;
            SystemDiskRead = systemDiskRead;
            SystemDiskWrite = systemDiskWrite;
        }

        public PerfDataRecord() { }
        public string TimeStamp { get; set; }
        public string Marker { get; set; }
        public string Time { get; set; }
        public string Thread { get; set; }
        public string CpuUsed { get; set; }
        public string CpuTotal { get; set; }
        public string ResidentSize { get; set; }
        public string VirtualSize { get; set; }
        public string WifiSent { get; set; }
        public string WifiRecv { get; set; }
        public string WwanSent { get; set; }
        public string WwanRecv { get; set; }
        public string AppSent { get; set; }
        public string AppRecv { get; set; }
        public string Battery { get; set; }
        public string SystemDiskRead { get; set; }
        public string SystemDiskWrite { get; set; }
        public string MarkerName { get; set; } = ValueNotApplicable;
        public double GetMsResponseTime() => double.Parse(Time) / 1000;
        public double GetKbResidentMemory() => double.Parse("0.0") / 1000;
        public double GetKbVirtualMemory() => double.Parse("0.0") / 1000;
    }
}
