md "%HOMEPATH%\_delete_content\"
pushd %~dp0\..\..
if not exist ".\target\uberjar\mnemosyne-uberjar.jar" (
  rmdir /s /q target
  call lein do clean, uberjar
)
call java -cp .\target\uberjar\mnemosyne-uberjar.jar pl.tomaszgigiel.mnemosyne.core
pause
popd
