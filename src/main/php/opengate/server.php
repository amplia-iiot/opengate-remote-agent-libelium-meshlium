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
*  Autor: Diego Becerrica
*
*/

require_once $cloud_plugin.'/php/functions.php';

switch($_POST['type']){
    case "start_cloud": start_cloud(); break;
    case "stop_cloud": stop_cloud(); break;
    case "save_cloud": save_cloud($_POST); break;
}

?>