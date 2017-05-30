<?php
/*
 *  Copyright (C) 2008 Libelium Comunicaciones Distribuidas S.L.
 *  http://www.libelium.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *                                                        )[            ....   
                                                       -$wj[        _swmQQWC   
                                                        -4Qm    ._wmQWWWW!'    
                                                         -QWL_swmQQWBVY"~.____ 
                                                         _dQQWTY+vsawwwgmmQWV! 
                                        1isas,       _mgmQQQQQmmQWWQQWVY!"-    
                                       .s,. -?ha     -9WDWU?9Qz~- -- -         
                                       -""?Ya,."h,   <!`_mT!2-?5a,             
                                       -Swa. Yg.-Q,  ~ ^`  /`   "$a.           
     aac  <aa, aa/                aac  _a,-4c ]k +m               "1           
    .QWk  ]VV( QQf   .      .     QQk  )YT`-C.-? -Y  .                         
    .QWk       WQmymmgc  <wgmggc. QQk       wgz  = gygmgwagmmgc                
    .QWk  jQQ[ WQQQQQQW;jWQQ  QQL QQk  ]WQ[ dQk  ) QF~"WWW(~)QQ[               
    .QWk  jQQ[ QQQ  QQQ(mWQ9VVVVT QQk  ]WQ[ mQk  = Q;  jWW  :QQ[               
     WWm,,jQQ[ QQQQQWQW')WWa,_aa. $Qm,,]WQ[ dQm,sj Q(  jQW  :QW[               
     -TTT(]YT' TTTYUH?^  ~TTB8T!` -TYT[)YT( -?9WTT T'  ]TY  -TY(               
                     
                          www.libelium.com

*  Libelium Comunicaciones Distribuidas SL
*  Autor: Amplia Soluciones
* 
*/

$cloudPropsFilepath="/mnt/lib/cfg/OpenGate/agent.properties";


/*
 * Get configuration values from file and stores them in an array.
 * Change the dummy names for self-explained names (url, user, password...)
 */
function getSetup(){
    global $cloudPropsFilepath;
    $arrayconf = parse_ini_file($cloudPropsFilepath);
    return $arrayconf;
}

function save_cloud($values){
    global $cloudPropsFilepath;
    
    $cloudProps = getSetup();
    
    $cloudProps['connection.http.remote.address'] = $values['ogserver'];
    $cloudProps['connection.http.remote.port'] = $values['ogport'];
    $cloudProps['connection.common.apiKey'] = $values['api'];
    
    write_ini_file($cloudProps, $cloudPropsFilepath);
}

function start_cloud(){
    exec("sudo /bin/OpenGateD.sh start >/dev/null 2>/dev/null &");
    echo "ok";
}

function stop_cloud(){
    exec("sudo /bin/OpenGateD.sh stop");
    echo "ok";
}

/*
 * Form to enter the configuration data. Is preloaded with current setup.
 */
function OGSetupGet(){
    global $section, $plugin, $cloud;
    
    $CurrentSetup = getSetup();
    
    $html=
    '<tr><td>
        <label><i class="fa fa-cloud" aria-hidden="true"></i> Server host</label>
        </td><td>
        <input maxlength="255" type="text" class="ms_hex" name="ogserver" id="ogserver"  value="'.$CurrentSetup['connection.http.remote.address'].'" />
    </td></tr>
    <tr><td>
        <label><i class="fa fa-circle-o-notch" aria-hidden="true"></i> Server port</label>
        </td><td>
        <input maxlength="5" type="text" class="ms_hex" name="ogport" id="ogport" value="'.$CurrentSetup['connection.http.remote.port'].'" />
    </td></tr>

    <tr><td>
        <label><i class="fa fa-key" aria-hidden="true"></i> Api-Key</label>
        </td><td>
        <input maxlength="36" type="password" class="ms_hex" name="api" id="api" value="'.$CurrentSetup['connection.common.apiKey'].'" />
    </td></tr>

    <tr><td colspan="2" align="center">
        <button type="button" class="save_button" onclick="save_cloud(\''.$section.'\',\''.$plugin.'\',\''.$cloud.'\')"><i class="fa fa-floppy-o"></i>  Save</button>
    </td></tr>
    ';
    
    return $html;
}

function write_ini_file($assoc_arr, $filePath) {
    $content = "";

    foreach ($assoc_arr as $key=>$elem) {
            if($elem=="") {
                if ($key == 'database.synchronization.markAsSynchronized' ) {
                    $content .= $key."=false\n";   
                } else {
                    $content .= $key."=\n";
                }
            }
            else 
            {
                if ($key == 'database.synchronization.markAsSynchronized' ) {
                    if($elem=="1") {
                        $content .= $key."=true\n";    
                    } else {
                        $content .= $key."=false\n";    
                    }
                } else {
                    $content .= $key."=".$elem."\n";
                }
            }
    }


    if (!$handle = fopen($filePath, 'w')) {
        return false;
    }

    $success = fwrite($handle, $content);
    fclose($handle);

    return $success;
}

?>