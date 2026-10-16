@echo off
echo ========================================================
echo Starting EduTrack Engine...
echo ========================================================
if exist "C:\Users\Dell\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.11.v20260515-1531\jre\bin\java.exe" (
    "C:\Users\Dell\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.11.v20260515-1531\jre\bin\java.exe" -cp bin edutrack.cli.EduTrackMain
) else (
    java -cp bin edutrack.cli.EduTrackMain
)
pause
