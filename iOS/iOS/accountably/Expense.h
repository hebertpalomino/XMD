//
//  Expense.h
//  accountably
//
//  Created by Steve Palomino on 1/27/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Expense : NSObject
{
    
}

@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) NSString *objectID;
@property (nonatomic, strong) NSNumber *amount;
@property (nonatomic, strong) NSNumber *aPriority;



@end
