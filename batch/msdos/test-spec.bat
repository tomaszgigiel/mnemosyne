md "%HOMEPATH%\_delete_content\"
pushd %~dp0\..\..
call lein test :only pl.tomaszgigiel.mnemosyne
pause
popd
