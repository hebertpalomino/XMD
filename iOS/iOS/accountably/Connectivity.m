//
//  Connectivity.m
//  accountably
//
//  Created by Steve Palomino on 1/28/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import "Connectivity.h"
#import <SystemConfiguration/SystemConfiguration.h>
#import "Reachability.h"

@implementation Connectivity

-(BOOL)isConnected
{

    Reachability *reachability = [Reachability reachabilityForInternetConnection];
    NetworkStatus networkStatus = [reachability currentReachabilityStatus];
    return networkStatus != NotReachable;
    
}
@end
