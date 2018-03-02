rem Checks miner in a loop
@echo off
set PROFILE=xmrstak


:check
echo "Checking miner..."
java -cp .\mchecker-0.0.4.jar;.\dependency\* -Dspring.profiles.active=%PROFILE% -Djava.util.logging.config.file=logging.properties my.app.mchecker.MChecker
if %errorlevel% neq 0 (
  echo "Miner error, rebooting..."
  shutdown /r
  exit
) else (
  echo "Miner OK, sleeping..."
  choice /c a /d a /t 60
)
goto check
