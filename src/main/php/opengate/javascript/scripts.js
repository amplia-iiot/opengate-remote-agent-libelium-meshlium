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
*  Autor: Carlos Arilla, Diego Becerrica
*
*/

/* start_cloud(section,plugin,cloud)
 * This function sends an HTTP POST request to server.php with the type
 * "start_cloud". You can add new parameters.
 */
function start_cloud(section,plugin,cloud)
{
    submit_data="section="+section+"&plugin="+plugin+"&cloud="+cloud+"&type=start_cloud";
    $.ajax({
        type: "POST",
        url: "index.php",
        data: submit_data,
        beforeSend: function() {
            $('#daemonStatus').removeClass("stopped");
            $('#daemonStatus').removeClass("running");
            $('#daemonStatus').addClass("loading");
        },
        error: function(xhr) {
            notify("fail.png", "Error occurred. Please, refresh the page.");
        },
        success: function(datos) {
            $('#daemonStatus').removeClass("loading");
            $('#daemonStatus').addClass("running");
        }
    });
}

/* stop_cloud(section,plugin,cloud)
 * This function sends an HTTP POST request to server.php with the type
 * "stop_cloud". You can add new parameters.
 */
function stop_cloud(section,plugin,cloud)
{
    submit_data="section="+section+"&plugin="+plugin+"&cloud="+cloud+"&type=stop_cloud";
    $.ajax({
        type: "POST",
        url: "index.php",
        data: submit_data,
        beforeSend: function() {
            $('#daemonStatus').removeClass("stopped");
            $('#daemonStatus').removeClass("running");
            $('#daemonStatus').addClass("loading");
        },
        error: function(xhr) {
            notify("fail.png", "Error occurred. Please, refresh the page.");
        },
        success: function(datos) {
            $('#daemonStatus').removeClass("loading");
            $('#daemonStatus').addClass("stopped");
        }
    });
}

/* save(section,plugin,cloud)
 * This function sends an HTTP POST request to server.php with the type
 * "save_cloud" and the parameters to store in the setup file.
 * You can add new parameters.
 */
function save_cloud(section,plugin,cloud)
{
    //Take values from the form by #id:
    var form = $("#setup").serialize();
    //send POST DATA
    submit_data="section="+section+"&plugin="+plugin+"&cloud="+cloud+"&type=save_cloud&"+form;
    $.ajax({
        type: "POST",
        url: "index.php",
        data: submit_data,
        beforeSend: function() {
            saveAlert();
        },
        error: function(xhr) {
            notify("fail.png", "Error occurred. Please, refresh the page.");
        },
        success: function(datos){
            fadenotify();
        }
    });
}