#!/bin/bash

run_shutdown=0
if [ "$1" == "--shutdown-on-exit" ]; then
	run_shutdown=1
fi

PLAYER_PID=0
CHROMIUM_PID=0
UNCLUTTER_PID=0

cleanup() {
	[[ $PLAYER_PID -ne 0 ]]    && kill $PLAYER_PID    >/dev/null 2>/dev/null
	[[ $CHROMIUM_PID -ne 0 ]]  && kill $CHROMIUM_PID  >/dev/null 2>/dev/null
	[[ $UNCLUTTER_PID -ne 0 ]] && kill $UNCLUTTER_PID >/dev/null 2>/dev/null

	if [ $run_shutdown -eq 1 ]; then
		echo sudo /sbin/shutdown
	else
		xset dpms $xset_dpms
		xset s $xset_s1
		xset s $xset_s2
	fi
}

cd "$(dirname "$(readlink -f "$0")")"/..
pwd

xset_dpms=$(xset q | sed -n '/^DPMS/,+1p' | tail -n 1 | awk '{ print $2 " " $4 " " $6 }')

# shellcheck disable=SC2046
if [ $(xset q | sed -n '/^Screen Saver/,+1p' | tail -n 1 | awk '{ print $3 }') == 'no' ]; then
	xset_s1=blank
else
	xset_s1=noblank
fi

# shellcheck disable=SC2046
if [ $(xset q | sed -n '/^Screen Saver/,+2p' | tail -n 1 | awk '{ print $2 }') == '0' ]; then
	xset_s2=off
else
	xset_s2=on
fi

xset s noblank
xset s off
xset -dpms

trap cleanup EXIT

bin/player \
	--application.ui.exitType=QUIT \
	>/tmp/soundscapeplayer.log \
	2>&1 \
	&
PLAYER_PID=$!


unclutter -root &
UNCLUTTER_PID=$!


while [[ "$(curl -s -o /dev/null -w '%{http_code}' 127.0.0.1:8008)" != "200" ]]; do
	sleep 2
done

sed -i 's/"exited_cleanly":false/"exited_cleanly":true/' "$HOME/.config/chromium/Default/Preferences"
sed -i 's/"exit_type":"Crashed"/"exit_type":"Normal"/' "$HOME/.config/chromium/Default/Preferences"


chromium --kiosk http://127.0.0.1:8008/ &
CHROMIUM_PID=$!


wait $PLAYER_PID
